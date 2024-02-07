package com.stpa.ws.firma.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerId;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import com.stpa.ws.pref.circe.Preferencias;
import com.stpa.ws.server.util.Logger;

public class Pkcs7Verify {
	
	private X509Certificate[] _IntCert= null;
	private X509Certificate _Scert= null;
	private String _SubjectDN= null;
	private String _textFile= null;
	private String _pkcs7File= null;
	private byte[] _signature= null;
	private RSAPublicKey _pk= null;
	Preferencias pref = new Preferencias();
 
	/**
	 * Constructor inicial con certificados.
	 * @param certs Ruta certificados.
	 * @param textFile Contenido a verificar.
	 * @param pkcs7 Firma digital con contenido para la verificacion.
	 * @throws Exception error durante el proceso de verificacion.
	 */
	Pkcs7Verify(String[] certs, String textFile, String pkcs7){
		loadCerts(certs);
		_textFile= textFile;
		_pkcs7File= pkcs7;
	}
	/**
	 * Constructor inicial
	 * @param textFile Ruta fichero Contenido a verificar.
	 * @param pkcs7 Ruta fichero Firma digital con contenido para la verificacion.
	 * @throws Exception error durante el proceso de verificacion.
	 */
	public Pkcs7Verify( String textFile, String pkcs7) throws Exception{
		
		_textFile= textFile;
		_pkcs7File= pkcs7;
		
	}
	private void loadCerts(String[] intCertFiles) {
		try {
			_IntCert= new X509Certificate[intCertFiles.length];
			for (int i=0; i<intCertFiles.length;i++){ 
				InputStream inStream = new FileInputStream(intCertFiles[i]);
				CertificateFactory cf = CertificateFactory.getInstance("X.509");
				_IntCert[i] = (X509Certificate)cf.generateCertificate(inStream);
				inStream.close();
			}
		}
		catch (Exception e){
			com.stpa.ws.server.util.Logger.error("Pkcs7Verify:loadCerts(). Exception:" + e.getMessage(), e,Logger.LOGTYPE.APPLOG);
		}
 
	}
	/**
	 * Tratamiento byte array hacia hexadecimal.
	 * @param bytes Byte array de datos a tratar.
	 */
	@SuppressWarnings("static-access")
	public static void hexPrint(byte[] bytes) {
		try{
			Hex hexEncoder= new Hex();
			hexEncoder.encode(bytes,0,bytes.length,System.out);
		}
		catch (Exception e){
			com.stpa.ws.server.util.Logger.error("Pkcs7Verify,hexPrint:" + e.getMessage(), e,Logger.LOGTYPE.APPLOG);
		}
	}
	
	/**
	 * Inicializa proveedor de funcionalidades criptograficas BouncyCastle.
	 */
	private void providerInit() {
		if (Security.getProvider("BC") == null) {
			BouncyCastleProvider bcp = new BouncyCastleProvider();
			Security.insertProviderAt(bcp, 2);
		}
	}
   /**
    * Verificar contra el Certificado.
    * @param certs Objeto del almacen de certificados. 
    * @return Boolan indicando si la operacion se ha podido realizar.
    */
	@SuppressWarnings({ "unchecked", "unused" })
	private boolean verifyAgainstCA(CertStore certs){
		boolean result= false;
 
		try{	
			List certChain= new ArrayList();
			X509Certificate rootCert= _IntCert[0];
			Collection certCollection = certs.getCertificates(null);
			certChain.add( rootCert  );
 
 
			for (int i=1;i<_IntCert.length;i++)
				certChain.add(_IntCert[i]);
 
			Iterator certIt = certCollection.iterator();
 
			for (int i=0; i< certCollection.size();i++){	
				certChain.add ((X509Certificate)certIt.next());
			}
 
 
			CollectionCertStoreParameters ccsp= new CollectionCertStoreParameters( certChain );
			CertStore store= CertStore.getInstance("Collection", ccsp);
			CertPath cp = CertificateFactory.getInstance("X.509","BC").generateCertPath( certChain );
 
			Set<TrustAnchor> trust= new HashSet<TrustAnchor>();
			trust.add( new TrustAnchor( rootCert , null ));
 
			PKIXParameters param= new PKIXParameters( trust );
			param.addCertStore(store);
			param.setRevocationEnabled(false);
			param.setTrustAnchors(trust);
 
			CertPathValidator cpv= CertPathValidator.getInstance("PKIX","BC");
 
			try{
				cpv.validate(cp,param);
				result= true;
				com.stpa.ws.server.util.Logger.info("Pkcs7Verify,verifyAgainstCA:Verificacion de la cadena de certificacion correcta.",Logger.LOGTYPE.APPLOG);
			}
			catch(CertPathValidatorException e){
				result= false;
				com.stpa.ws.server.util.Logger.error("Pkcs7Verify,verifyAgainstCA:error:" + e.getMessage(), e,Logger.LOGTYPE.APPLOG);
			}
		}
		catch (Exception e){
			com.stpa.ws.server.util.Logger.error("Pkcs7Verify,verifyAgainstCA:error:" + e.getMessage(), e,Logger.LOGTYPE.APPLOG);
		}
		return result;
	}	
	/**
	 * SHADiget para SHA-1
	 * @return byte array del mensaje.
	 */
	public byte[] SHA1Digest() {
		byte[] digest= null;
 
		try{	
			// Usaremos SHA-1
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");                        
 
			// Calculamos SHA-1
			messageDigest.update( ExtraccionYTratamientoFicheros.readFile(_textFile) );
 
			digest= messageDigest.digest();
		}
		catch (Exception e){
			com.stpa.ws.server.util.Logger.error("Pkcs7Verify,SHA1Digest:Error:." + e.getMessage(), e,Logger.LOGTYPE.APPLOG);
		}
		return digest;
	}	
	/**
	 * Comprueba que la firma es correcta, que corresponde a los 
	 * datos en texto plano y que ademas, la cadena de certificacion
	 * ofrecida como primer argumento del constructor, valida. 
	 * @param aSignedData Fichero firmado.
	 * @return Boolean indicando si se ha podido comprobar el objeto formado con su certificado publico.
	 * @throws CMSException Excepcion.
	 * @throws NoSuchProviderException Excepcion.
	 * @throws NoSuchAlgorithmException Excepcion.
	 * @throws CertStoreException Excepcion.
	 * @throws CertificateNotYetValidException Excepcion.
	 * @throws CertificateExpiredException Excepcion.
	 */
	@SuppressWarnings("unchecked")
	private boolean verifyCMS(CMSSignedData aSignedData) throws CMSException, NoSuchProviderException, NoSuchAlgorithmException, CertStoreException, CertificateNotYetValidException, CertificateExpiredException {
 
		providerInit();
		CertStore certs = aSignedData.getCertificatesAndCRLs("Collection", "BC"); 
 
		SignerInformationStore signers = aSignedData.getSignerInfos();
		Collection c = signers.getSigners();
		Iterator it = c.iterator();
		boolean verificationResult = false;
		while (it.hasNext())
		{			
			SignerInformation signer = (SignerInformation)it.next();
			SignerId signerId = signer.getSID();
			Collection certCollection = certs.getCertificates(signerId);
			
			Iterator certIt= certCollection.iterator();
			X509Certificate cert= (X509Certificate)certIt.next();
			_SubjectDN= cert.getSubjectDN().toString();
			_Scert= cert;
			
			verificationResult= signer.verify(cert, "BC");
			
		}
		return verificationResult;
	}
	/**
	 * Inicializar la verificacion Certificado y Firma.
	 * @param aDataBytes Contenido en byte array.
	 * @param aSignature Firma en byte array.
	 * @return Boolean indicando si se ha podido realizar la verificacion.
	 * @throws CMSException Excepcion.
	 * @throws NoSuchProviderException Excepcion.
	 * @throws NoSuchAlgorithmException Excepcion.
	 * @throws CertStoreException Excepcion.
	 * @throws CertificateNotYetValidException Excepcion.
	 * @throws CertificateExpiredException Excepcion.
	 */
	private boolean verifyCAandSignature(byte[] aDataBytes, byte[] aSignature) throws CMSException,	NoSuchProviderException, NoSuchAlgorithmException, CertStoreException, CertificateNotYetValidException, CertificateExpiredException {
 
		providerInit();
 
		CMSProcessableByteArray cmsByteArray = new CMSProcessableByteArray(aDataBytes);
		CMSSignedData cmsSd = new CMSSignedData(cmsByteArray, aSignature);
		
		//OutputStream aSalida = new OutputStream("");
		StringBuffer sb = new StringBuffer();
		for (int i=1; i<aDataBytes.length;i++){
			sb.append(Byte.toString(aDataBytes[i-1]));
		}
		
		return verifyCMS(cmsSd);		
	}
	/**
	 * 
	 * @param aDataBytes Contenido en byte array.
	 * @param is Input stream de la firma.
	 * @return Boolean indicando si se ha podido realizar la verificacion.
	 * @throws CMSException Excepcion.
	 * @throws NoSuchProviderException Excepcion.
	 * @throws NoSuchAlgorithmException Excepcion.
	 * @throws CertStoreException Excepcion.
	 * @throws CertificateNotYetValidException Excepcion.
	 * @throws CertificateExpiredException Excepcion.
	 */
	@SuppressWarnings("unused")
	private boolean verifyCAandSignature(byte[] aDataBytes, InputStream is)	throws CMSException,NoSuchProviderException,NoSuchAlgorithmException,CertStoreException,CertificateNotYetValidException,CertificateExpiredException {
 
		providerInit();
 
		CMSProcessableByteArray cmsByteArray = new CMSProcessableByteArray(aDataBytes);
		CMSSignedData cmsSd = new CMSSignedData(cmsByteArray, is);
		
		return verifyCMS(cmsSd);		
	}
	/**
	 * Metodo principal de verificacion CONTENIDO contra FIRMA.
	 * @return Boolean indicando si se ha podido realizar la verificacion.
	 */
	public boolean verifyPkcs7(){
		boolean result= false;
 
		try{
			
			com.stpa.ws.server.util.Logger.info("verifyPkcs7; comienza la verificacion de firma.",Logger.LOGTYPE.APPLOG);	  
 
			byte[] _pkcs7Pem= ExtraccionYTratamientoFicheros.readFile(_pkcs7File);
			byte[] derPkcs7= Base64.decode(_pkcs7Pem);
			byte[] dataBytesPlainText= ExtraccionYTratamientoFicheros.readFile(_textFile);
			
			result= verifyCAandSignature(dataBytesPlainText, derPkcs7);
			
			if (!result){ 
				try{					
					java.io.FileOutputStream writerDocumento = new java.io.FileOutputStream(_textFile, false);
					String hilo = Double.toString(Math.random());					
					String pathDummy = pref.getM_pathSolresp()+hilo+".txt";
					
					java.io.FileOutputStream fOutput = new java.io.FileOutputStream(pathDummy, false);
					
					fOutput.write(dataBytesPlainText);
					fOutput.flush();
					fOutput.close();


					CRLFOutputStream crlf = new CRLFOutputStream(writerDocumento);
					crlf.write(ExtraccionYTratamientoFicheros.readFile(pathDummy));
					
					writerDocumento.flush();
					writerDocumento.close();
					
					dataBytesPlainText = ExtraccionYTratamientoFicheros.readFile(_textFile);					

					
					result = verifyCAandSignature(dataBytesPlainText, derPkcs7);
					
					if(result) {
						ExtraccionYTratamientoFicheros.borradoFichero(pathDummy);
					}
					
				}
				catch( CMSException ex ){
					result= false;
					com.stpa.ws.server.util.Logger.error("Pkcs7Verify:verifyPkcs7. ERROR." + ex.getMessage(), ex,Logger.LOGTYPE.APPLOG);
				}
			}			
		}
		catch(Exception e){
			com.stpa.ws.server.util.Logger.error("Pkcs7Verify:verifyPkcs7. ERROR." + e.getMessage(), e,Logger.LOGTYPE.APPLOG);
		}
		com.stpa.ws.server.util.Logger.info("Pkcs7Verify:verifyPkcs7. El resultado de la validacion de la firma es : "+ result,Logger.LOGTYPE.APPLOG);
		return result;
	}
 
 
	public String getX509SubjectName(){
		return _SubjectDN;
	}
 
	public byte[] getSignature(){
		return _signature;
	}
 
	public RSAPublicKey getPublicKey(){
		return _pk;
	}
	/**
	 * Obtenemos el Certificado codificado.
	 * @return String Certificado codificado en base64.
	 */
	public String getX509Certificate(){
		try{
			byte[] bytes = _Scert.getEncoded();
			String encoded = new String(new sun.misc.BASE64Encoder().encode(bytes));			
			return encoded;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
}
