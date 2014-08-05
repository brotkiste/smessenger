package client;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Date;
import java.util.Iterator;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.CompressionAlgorithmTags;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.crypto.util.SubjectPublicKeyInfoFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.examples.KeyBasedFileProcessor;
import org.bouncycastle.openpgp.examples.PGPExampleUtil;
import org.bouncycastle.openpgp.examples.RSAKeyPairGenerator;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPDigestCalculatorProviderBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyDataDecryptorFactoryBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;
import org.bouncycastle.util.io.Streams;


public class C_Krypto {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Security.addProvider(new BouncyCastleProvider());
		//test();
		//keyGen("Matthias", "Test-Key", "asdf@example.org", "passwd");
		
		
		PGPPublicKey pubKey;
		PGPPublicKey secretKey;
	

		try {
			pubKey = PGPExampleUtil.readPublicKey("pub.asc");
			
			
			String encrypted = encryptString("Alle meine Entchen", pubKey);
			
			System.out.println(decryptString(encrypted,"secret.asc","passwd"));			
			

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PGPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	/**
	 * Verschlüsselt einen String mit einem öffentlichen PGP Schlüssel
	 * @param text		String, der verschlüsselt werden soll
	 * @param encKey	PGPPublicKey Objekt, mit dem verschlüsselt werden soll
	 * @return			verschlüsselte Nachricht als String
	 */
	public static String encryptString(String text, PGPPublicKey encKey){
		
		String result = "";
		
		try {
		
			
			File tmpfile = new File("tmp.txt");
			FileOutputStream fout = new FileOutputStream(tmpfile);
		
			fout.write(text.getBytes());
			fout.close();
			
			result = encryptFile("tmp.txt", encKey);
			
			tmpfile.delete();
			
		} catch (IOException | NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	/**
	 * Entschlüsselt eine Nachricht in einem String
	 * @param encrypted					verschlüsselte Nachricht als String
	 * @param filenameForPrivateKey		Datei, in der nach dem PrivateKey gesucht werden soll
	 * @param passwd					Passwort, mit dem der PrivateKey geschützt ist
	 * @return							entschlüsselte Nachricht als String
	 */
	public static String decryptString(String encrypted, String filenameForPrivateKey, String passwd){
		try {
			return decryptFile((InputStream)(new BufferedInputStream(new ByteArrayInputStream(encrypted.getBytes(StandardCharsets.UTF_8)))),(InputStream)(new BufferedInputStream(new FileInputStream(filenameForPrivateKey))), passwd.toCharArray());
		} catch (NoSuchProviderException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Verschlüsselt eine Datei mit einem öffentlichen PGP Schlüssel
	 * @param fileName					Datei, die verschlüsselt werden soll
	 * @param encKey					PGPPublicKey Objekt, mit dem verschlüsselt werden soll
	 * @return							verschlüsselte Datei als String
	 * @throws IOException
	 * @throws NoSuchProviderException
	 */
	public static String encryptFile(
	        String          fileName,
	        PGPPublicKey    encKey)
	        //boolean         armor,
	        //boolean         withIntegrityCheck)
	        throws IOException, NoSuchProviderException
	    {
		
	        boolean armor = true;
	        boolean withIntegrityCheck = true;
	        
	        String result = "";
	        	
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			OutputStream out = new BufferedOutputStream(baos);
		
	        if (armor)
	        {
	            out = new ArmoredOutputStream(out);
	        }

	        try
	        {
	            byte[] bytes = C_Krypto.compressFile(fileName, CompressionAlgorithmTags.ZIP);

	            PGPEncryptedDataGenerator encGen = new PGPEncryptedDataGenerator(
	                new JcePGPDataEncryptorBuilder(PGPEncryptedData.CAST5).setWithIntegrityPacket(withIntegrityCheck).setSecureRandom(new SecureRandom()).setProvider("BC"));

	            encGen.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(encKey).setProvider("BC"));

	            OutputStream cOut = encGen.open(out, bytes.length);

	            cOut.write(bytes);
	            cOut.close();
	            
				out.close();
				
				result = baos.toString();
			
				baos.close();

	            if (armor)
	            {
	                out.close();
	            }
	        }
	        catch (PGPException e)
	        {
	            System.err.println(e);
	            if (e.getUnderlyingException() != null)
	            {
	                e.getUnderlyingException().printStackTrace();
	            }
	        }
			return result;
	    }
	
	/**
	 * Komprimiert eine Datei
	 * @param fileName					Datei, die komprimiert werden soll
	 * @param algorithm					Komprimierungsalgorithmus --> CompressionAlgorithmTags
	 * @return
	 * @throws IOException
	 */
	private static byte[] compressFile(String fileName, int algorithm) throws IOException {
	        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
	        PGPCompressedDataGenerator comData = new PGPCompressedDataGenerator(algorithm);
	        PGPUtil.writeFileToLiteralData(comData.open(bOut), PGPLiteralData.BINARY,
	            new File(fileName));
	        comData.close();
	        return bOut.toByteArray();
	}

	public static String decryptFile(
	        InputStream in,
	        InputStream keyIn,
	        char[]      passwd)
	        //String      defaultFileName)
	        throws IOException, NoSuchProviderException
	    {
	        in = PGPUtil.getDecoderStream(in);
	        
	        String result = "";
	        
	        try
	        {
	            PGPObjectFactory pgpF = new PGPObjectFactory(in);
	            PGPEncryptedDataList    enc;

	            Object                  o = pgpF.nextObject();
	            //
	            // the first object might be a PGP marker packet.
	            //
	            if (o instanceof PGPEncryptedDataList)
	            {
	                enc = (PGPEncryptedDataList)o;
	            }
	            else
	            {
	                enc = (PGPEncryptedDataList)pgpF.nextObject();
	            }
	            
	            //
	            // find the secret key
	            //
	            Iterator                    it = enc.getEncryptedDataObjects();
	            PGPPrivateKey               sKey = null;
	            PGPPublicKeyEncryptedData   pbe = null;
	            PGPSecretKeyRingCollection  pgpSec = new PGPSecretKeyRingCollection(
	                PGPUtil.getDecoderStream(keyIn));

	            while (sKey == null && it.hasNext())
	            {
	                pbe = (PGPPublicKeyEncryptedData)it.next();
	                
	                sKey = PGPExampleUtil.findSecretKey(pgpSec, pbe.getKeyID(), passwd);
	            }
	            
	            if (sKey == null)
	            {
	                throw new IllegalArgumentException("secret key for message not found.");
	            }
	    
	            InputStream         clear = pbe.getDataStream(new JcePublicKeyDataDecryptorFactoryBuilder().setProvider("BC").build(sKey));
	            
	            PGPObjectFactory    plainFact = new PGPObjectFactory(clear);
	            
	            Object              message = plainFact.nextObject();
	    
	            if (message instanceof PGPCompressedData)
	            {
	                PGPCompressedData   cData = (PGPCompressedData)message;
	                PGPObjectFactory    pgpFact = new PGPObjectFactory(cData.getDataStream());
	                
	                message = pgpFact.nextObject();
	            }
	            
	            if (message instanceof PGPLiteralData)
	            {
	                PGPLiteralData ld = (PGPLiteralData)message;

	                /*
	                String outFileName = ld.getFileName();
	                if (outFileName.length() == 0)
	                {
	                    outFileName = defaultFileName;
	                }
	                */

	                InputStream unc = ld.getInputStream();
	                //OutputStream fOut = new BufferedOutputStream(new FileOutputStream(outFileName));
	                ByteArrayOutputStream baos = new ByteArrayOutputStream();
	                OutputStream fOut = new BufferedOutputStream(baos);

	                Streams.pipeAll(unc, fOut);

	                fOut.close();
	                baos.close();
	                result = baos.toString();
	                
	            }
	            else if (message instanceof PGPOnePassSignatureList)
	            {
	                throw new PGPException("encrypted message contains a signed message - not literal data.");
	            }
	            else
	            {
	                throw new PGPException("message is not a simple encrypted file - type unknown.");
	            }

	            if (pbe.isIntegrityProtected())
	            {
	                if (!pbe.verify())
	                {
	                    System.err.println("message failed integrity check");
	                }
	                else
	                {
	                    System.err.println("message integrity check passed");
	                }
	            }
	            else
	            {
	                System.err.println("no message integrity check");
	            }
	        }
	        catch (PGPException e)
	        {
	            System.err.println(e);
	            if (e.getUnderlyingException() != null)
	            {
	                e.getUnderlyingException().printStackTrace();
	            }
	        }
			return result;
	    }
	

	/**
	 * @deprecated
	 *
	public static void encrypt(){
		try {
			KeyBasedFileProcessor.main(new String[]{"-e","-a", "datei.txt", "pub.asc"});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/

	
	/**
	 * @deprecated
	 *
	public static void decrypt(){
		try {
			KeyBasedFileProcessor.main(new String[]{"-d", "datei.txt.asc", "secret.asc", "passwd"});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	
	/**
	 * @deprecated
	 * @param text
	 * @param encKey
	 * @return
	 *
	public static String encrypt(String text, PGPPublicKey encKey){
		
		String result = "";
		
		
		
		byte[] bytes = text.getBytes();
				
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		OutputStream out = new BufferedOutputStream(baos);
		
        
		
		//try {
		//	out = new BufferedOutputStream(new FileOutputStream("encrypt.txt"));
		//} catch (FileNotFoundException e1) {
		//	// TODO Auto-generated catch block
		//	e1.printStackTrace();
		//}
		try {
			

			PGPLiteralDataGenerator lData = new PGPLiteralDataGenerator();
	        //out = lData.open(out, PGPLiteralData.UTF8, "filename", 1024, new Date());
	        
		
			out = new ArmoredOutputStream(out);
				
			PGPEncryptedDataGenerator encGen = new PGPEncryptedDataGenerator(new JcePGPDataEncryptorBuilder(PGPEncryptedData.CAST5).setWithIntegrityPacket(true).setSecureRandom(new SecureRandom()).setProvider("BC"));
		
			encGen.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(encKey).setProvider("BC"));
		
		
			OutputStream cOut = encGen.open(out, bytes.length);
			
			cOut.write(bytes.length);
			cOut.close();
			out.close();
			
			result = baos.toString();
		
			baos.close();
			
		} catch (IOException | PGPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
		
	}
*/

	
	/**
	 * @deprecated
	 * TODO eigene Implementierung
	 * @param name
	 * @param bezeichnung
	 * @param email
	 * @param passphrase
	 */
	public static void keyGen(String name, String bezeichnung, String email, String passphrase){
		try {
			RSAKeyPairGenerator.main(new String[]{"-a", name + " (" + bezeichnung + ") <" + email + ">", passphrase, " *.asc"});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static C_KeyPair genKeyPair(String name, String bezeichnung, String email, String passwd){
		
		C_KeyPair result = null;
		
		Security.addProvider(new BouncyCastleProvider());

		try {
        
			KeyPairGenerator    kpg = KeyPairGenerator.getInstance("RSA", "BC");
        
			//kpg.initialize(1024);--matthias
			kpg.initialize(4096);
			
			KeyPair                    kp = kpg.generateKeyPair();
            
			ByteArrayOutputStream    baossecretOut = new ByteArrayOutputStream();
			ByteArrayOutputStream    baospublicOut = new ByteArrayOutputStream();
			
			OutputStream secretOut = new BufferedOutputStream(baossecretOut);
			OutputStream publicOut = new BufferedOutputStream(baospublicOut);
            
			//exportKeyPair(out1, out2, kp.getPublic(), kp.getPrivate(), args[1], args[2].toCharArray(), true);
        
			secretOut = new ArmoredOutputStream(secretOut);
            
        	PGPDigestCalculator sha1Calc = new JcaPGPDigestCalculatorProviderBuilder().build().get(HashAlgorithmTags.SHA1);
        	PGPKeyPair          keyPair = new PGPKeyPair(PGPPublicKey.RSA_GENERAL, kp.getPublic(), kp.getPrivate(), new Date());
        	PGPSecretKey secretKey = new PGPSecretKey(PGPSignature.DEFAULT_CERTIFICATION, keyPair, name + " (" + bezeichnung + ") <" + email + ">", sha1Calc, null, null, new JcaPGPContentSignerBuilder(keyPair.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA1), new JcePBESecretKeyEncryptorBuilder(PGPEncryptedData.CAST5, sha1Calc).setProvider("BC").build(passwd.toCharArray()));
		
            
            secretKey.encode(secretOut);
            
            secretOut.close();
            
            C_SecKey sec = new C_SecKey(secretKey, baossecretOut.toString());
            
            publicOut = new ArmoredOutputStream(publicOut);

            PGPPublicKey    key = secretKey.getPublicKey();
            
            key.encode(publicOut);
            
            publicOut.close();
            
            C_PubKey pub = new C_PubKey(key, baospublicOut.toString());
            
            result = new C_KeyPair(sec, pub);
            
        } catch (PGPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
        
	}
	
	
	/**
	 * Sucht auf pgp.mit.edu nach einem öffentlichen Schlüssel
	 * @param searchphrase
	 * @return					PGPPublicKey Objekt, mit dem verschlüsselt werden kann
	 */
	public static C_PubKey lookup(String searchphrase){
		
		C_PubKey result = null;
		
		try {
			URL url = new URL("http://pgp.mit.edu/pks/lookup?op=get&search="+searchphrase);
			URLConnection con = url.openConnection();
			
			File tmpfile = new File("tmpLookup.txt");
			
			BufferedInputStream tmpin = new BufferedInputStream(con.getInputStream());
			BufferedOutputStream tmpout = new BufferedOutputStream(new FileOutputStream(tmpfile));
			
			BufferedReader inReader = new BufferedReader(new InputStreamReader(tmpin));
			
			String line = null;
			String lineout = null;
			boolean key = false;
			int counter = 0;
			
			String keyString = "";
			
			while ((line = inReader.readLine() ) != null) {
				if(line.equals("-----BEGIN PGP PUBLIC KEY BLOCK-----")){
					key = true;
				}
				if(key){
					counter++;
					//System.out.println(line);
					
					lineout = line + "\n";
					keyString += lineout;
					tmpout.write(lineout.getBytes(StandardCharsets.UTF_8));
				}
				if(line.equals("-----END PGP PUBLIC KEY BLOCK-----")){
					key = false;
				}
			}
			
			tmpout.flush();
			
			result = new C_PubKey(readPublicKey(new FileInputStream(tmpfile)), keyString);
			
			tmpin.close();
			tmpout.close();
			inReader.close();
			
			tmpfile.delete();
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PGPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
		
	}
	
	public static PGPPublicKey readPublicKeyString(String pubKeyString){

		PGPPublicKey result = null;
		
		try {
		
			
			File tmpfile = new File("tmp.txt");
			FileOutputStream fout = new FileOutputStream(tmpfile);
		
			fout.write(pubKeyString.getBytes());
			fout.close();
			
			result = readPublicKey(new FileInputStream(tmpfile));
			
			tmpfile.delete();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PGPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

 	public static PGPPublicKey readPublicKey(InputStream input) throws IOException, PGPException
    {
        PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(
            PGPUtil.getDecoderStream(input));

        //
        // we just loop through the collection till we find a key suitable for encryption, in the real
        // world you would probably want to be a bit smarter about this.
        //

        Iterator keyRingIter = pgpPub.getKeyRings();
        while (keyRingIter.hasNext())
        {
            PGPPublicKeyRing keyRing = (PGPPublicKeyRing)keyRingIter.next();

            Iterator keyIter = keyRing.getPublicKeys();
            while (keyIter.hasNext())
            {
                PGPPublicKey key = (PGPPublicKey)keyIter.next();

                if (key.isEncryptionKey())
                {
                    return key;
                }
            }
        }

        throw new IllegalArgumentException("Can't find encryption key in key ring.");
    }
	

	public static C_PubKey importPublicKey(InputStream input){
		
		PGPPublicKey keyObject = null;
		String keyString = "";
		
		try {
			keyObject = readPublicKey(input);
		
		
		BufferedReader inReader = new BufferedReader(new InputStreamReader(input));
		
		String line = null;
		String lineout = null;
		boolean key = false;
		int counter = 0;
		
		while ((line = inReader.readLine() ) != null) {
			if(line.equals("-----BEGIN PGP PUBLIC KEY BLOCK-----")){
				key = true;
			}
			if(key){
				counter++;
				//System.out.println(line);
				
				keyString += line + "\n";
			}
			if(line.equals("-----END PGP PUBLIC KEY BLOCK-----")){
				key = false;
			}
		}
		
		} catch (IOException | PGPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new C_PubKey(keyObject, keyString);
		
	}
	
	public static void test(){
		System.out.println( "\nSecurity-Provider:" );
		   for( Provider prov : Security.getProviders() ) {
		      System.out.println( "  " + prov + ": " + prov.getInfo() );
		   }
		   try{
		   System.out.println( "\nMaxAllowedKeyLength (fuer '" + Cipher.getInstance( "AES" ).getProvider() + "' mit aktuellen 'JCE Policy Files'):\n"
		         + "  DES        = " + Cipher.getMaxAllowedKeyLength( "DES"        ) + "\n"
		         + "  Triple DES = " + Cipher.getMaxAllowedKeyLength( "Triple DES" ) + "\n"
		         + "  AES        = " + Cipher.getMaxAllowedKeyLength( "AES"        ) + "\n"
		         + "  Blowfish   = " + Cipher.getMaxAllowedKeyLength( "Blowfish"   ) + "\n"
		         + "  RSA        = " + Cipher.getMaxAllowedKeyLength( "RSA"        ) + "\n" );
		   }catch(NoSuchPaddingException | NoSuchAlgorithmException e){
			   
		   }
	}
}
