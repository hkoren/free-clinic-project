SERVER:

keytool -genkey -alias client-alias -keyalg RSA -keypass changeit -storepass changeit -keystore keystore.jks

keytool -export -alias client-alias -storepass changeit -file client.cer -keystore keystore.jks

keytool -import -v -trustcacerts -alias client-alias -file client.cer  -keystore <J2EE_HOME>/domains/domain1/config/cacerts.jks -keypass changeit -storepass changeit


CLIENT: