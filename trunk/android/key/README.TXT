# Generate the client certificate.

keytool -genkey -alias client-alias -keyalg RSA -keypass changeit -storepass changeit -keystore keystore.jks
# Export the generated client certificate into the file client.cer.

keytool -export -alias client-alias -storepass changeit -file client.cer -keystore keystore.jks
# Add the certificate to the trust-store file <J2EE_HOME>/domains/domain1/config/cacerts.jks. Run keytool from the directory where you created the keystore and client certificate. Use the following parameters:

#  cacerts.jks was in <J2EE_HOME>/domains/domain1/config/

keytool -import -v -trustcacerts -alias client-alias -file client.cer -keystore cacerts.jks -keypass changeit -storepass changeit

The keytool utility returns this message:

Owner: CN=J2EE Client, OU=Java Web Services, O=Sun, L=Santa Clara, ST=CA, C=US
Issuer: CN=J2EE Client, OU=Java Web Services, O=Sun, L=Santa Clara, ST=CA, C=US
Serial number: 3e39e66a
Valid from: Thu Jan 30 18:58:50 PST 2003 until: Wed Apr 30
19:58:50 PDT 2003
Certificate fingerprints:
MD5: 5A:B0:4C:88:4E:F8:EF:E9:E5:8B:53:BD:D0:AA:8E:5A
SHA1:90:00:36:5B:E0:A7:A2:BD:67:DB:EA:37:B9:61:3E:26:B3:89:46:
32
Trust this certificate? [no]: yes
Certificate was added to keystore 