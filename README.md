Prerequisites
===
Maven 2
Spring Tool Suite 3.4 (recommended for development)

Configuration
===
Modify the src/main/resources/application.properties file:

```INI
initialresults=20
imageFolder=/some/shared/image/folder
imageCacheTime=172800

ldap.url=ldap://something@your.domain.com:389
ldap.base=dc=something
ldap.username=cn=something
ldap.referral=follow
ldap.password=secret
ldap.pageSize=25
ldap.returnedAttrs=mail,department,displayName,sAMAccountName,division,l,st,postalCode,streetAddress,title,physicalDeliveryOfficeName,mobile,telephoneNumber
```

or, if you prefer to have environment-specific variables, you can set the "propsFile" system variable:

```Shell
export propsFile=/path/to/your/application.properties
```

Just make sure that the file you point to at least contains the ldap.url, ldap.base, ldap.username and ldap.password. The src/main/webapp/resources/application.properties file is read in first and then the system specific propsFile will act as an override.

Build Project
===
```Shell
mvn clean package
```

Deploy Project
===
Copy the target/edirectory.war file to your tomcat7 webapps folder or equivalent folder in another application server.
