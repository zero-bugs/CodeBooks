import java.util.Arrays;

import java.net.URL;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 *  *
 *         VM Arguments
 *
 *         -Djavax.net.debug=ssl
 *         -Djavax.net.ssl.trustStore=/Users/amusarra/Documents/workspace-myBlog/http-ssl-client-example/src/main/resources/cacerts.jks
 *         -Djavax.net.ssl.trustStorePassword=changeit
 */
public class HttpTest
{
    private static TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
    {

        public java.security.cert.X509Certificate[] getAcceptedIssuers()
        {
            return new java.security.cert.X509Certificate[] {};
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType)
        {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
        {
        }
    }
    };

    static {
        // for localhost testing only
        javax.net.ssl.HttpsURLConnection
            .setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {

                public boolean verify(String hostname,
                    javax.net.ssl.SSLSession sslSession) {
//                    if (hostname.equals("localhost")) {
//                        return true;
//                    }
                    return true;
                }
            });
    }

    public static void main(String[] args)
    {
        System.out.println("parameter:" + Arrays.asList(args));
        if (args.length < 1)
        {
            System.out.println("lack of parameter.");
            System.exit(1);
        }

        String host = args[0];
        String URL = "https://" + host;

        HttpsURLConnection connection = null;
        try
        {
            // Create connection
            System.out.println("Try to connect to the URL " + URL + " ...");
            URL url = new URL(URL);
            connection = (HttpsURLConnection) url.openConnection();

            // Prepare a GET request Action
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml");
            connection.setRequestProperty("Accept-Language", "it-IT,it");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            // Create a SSL SocketFactory
            SSLSocketFactory sslSocketFactory = getFactorySimple();
            connection.setSSLSocketFactory(sslSocketFactory);

            System.out.println("HTTP Response Code:" + connection.getResponseCode());
            System.out.println("HTTP Response Message:" + connection.getResponseMessage());
            System.out.println("HTTP Content Length:" + connection.getContentLength());
            System.out.println("HTTP Content Type:" + connection.getContentType());
            System.out.println("HTTP Cipher Suite:" + connection.getCipherSuite());

            Certificate[] serverCertificate = connection.getServerCertificates();

            for (Certificate certificate : serverCertificate)
            {
                System.out.println("Certificate Type:" + certificate.getType());

                if (certificate instanceof X509Certificate)
                {
                    X509Certificate x509cert = (X509Certificate) certificate;

                    // Get subject
                    Principal principal = x509cert.getSubjectDN();
                    System.out.println("Certificate Subject DN:" + principal.getName());

                    // Get issuer
                    principal = x509cert.getIssuerDN();
                    System.out.println("Certificate IssuerDn:" + principal.getName());
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.fillInStackTrace());
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }
    }


    private static SSLSocketFactory getFactorySimple() throws Exception
    {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, trustAllCerts, null);
        return context.getSocketFactory();
    }

}
