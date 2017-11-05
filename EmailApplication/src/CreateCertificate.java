import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateIssuerName;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateSubjectName;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateVersion;
import sun.security.x509.CertificateX509Key;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

public class CreateCertificate {
	
	CreateCertificate(){
		
	}
	
	public X509Certificate generateCertificate(String dn, KeyPair pair, int days, String algorithm)
			throws GeneralSecurityException, IOException
	{
		PrivateKey privkey = pair.getPrivate();
		X509CertInfo info = new X509CertInfo();
		Date from = new Date();
		Date to = new Date(from.getTime() + days * 86400000l);
		CertificateValidity interval = new CertificateValidity(from, to);
		BigInteger sn = new BigInteger(64, new SecureRandom());
		X500Name owner = new X500Name("CN=" + dn);

		info.set(X509CertInfo.VALIDITY, interval);
		info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(sn));
		//info.set(X509CertInfo.SUBJECT, new CertificateSubjectName(owner));
		//info.set(X509CertInfo.ISSUER, new CertificateIssuerName(owner));
		boolean justName = isJavaAtLeast(1.8);
		        if (justName) {
		        	info.set(X509CertInfo.SUBJECT, owner);
		        	info.set(X509CertInfo.ISSUER, owner);
		        } else {
		        	info.set(X509CertInfo.SUBJECT, new CertificateSubjectName(owner));
		        	info.set(X509CertInfo.ISSUER, new CertificateIssuerName(owner));
		        }
		info.set(X509CertInfo.KEY, new CertificateX509Key(pair.getPublic()));
		info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
		AlgorithmId algo = new AlgorithmId(AlgorithmId.md5WithRSAEncryption_oid);
		info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algo));

		// Sign the cert to identify the algorithm that's used.
		X509CertImpl cert = new X509CertImpl(info);
		cert.sign(privkey, algorithm);

		// Update the algorith, and resign.
		algo = (AlgorithmId)cert.get(X509CertImpl.SIG_ALG);
		info.set(CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, algo);
		cert = new X509CertImpl(info);
		cert.sign(privkey, algorithm);
		return cert;
	}   
	
	public static final Pattern JAVA_VERSION = Pattern.compile("([0-9]*.[0-9]*)(.*)?");
	
	    /**
	     * Checks whether the current JAva runtime has a version equal or higher then the given one. As Java version are
	     * not double (because they can use more digits such as 1.8.0), this method extracts the two first digits and
     * transforms it as a double.
	     * @param version the version
	     * @return {@literal true} if the current Java runtime is at least the specified one,
	     * {@literal false} if not or if the current version cannot be retrieve or is the retrieved version cannot be
	     * parsed as a double.
	     */
	    public static boolean isJavaAtLeast(double version) {
	        String javaVersion = System.getProperty("java.version");
	        if (javaVersion == null) {
	            return false;
	        }
	
	        // if the retrieved version is one three digits, remove the last one.
	        Matcher matcher = JAVA_VERSION.matcher(javaVersion);
	        if (matcher.matches()) {
	            javaVersion = matcher.group(1);
	        }
	
	        try {
	            double v = Double.parseDouble(javaVersion);
	            return v >= version;
	        } catch (NumberFormatException e) { //NOSONAR
	            return false;
	        }
	     }

}
