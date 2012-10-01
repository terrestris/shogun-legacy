# SHOGun


SHOGun, a Java based WebGIS framework.

SHOGun is based on several high-quality Open Source frameworks, such as

  - Spring http://www.springsource.org/
  - Hibernate http://hibernate.org
  - OpenLayers http://openlayers.org
  - GeoExt http://geoext.org

## First steps

These first steps will get you up and running on a linux system. The notes described here were gathered and tested on an Ubuntu 12.04.

### Prerequesites

SHOGun is based upon [Maven](http://maven.apache.org/), so make sure it is installed on your system:

    $ (sudo) apt-get install maven2

We also need a servlet container to deploy SHOGun in; Let's use [Tomcat](http://tomcat.apache.org/) for this:

    $ (sudo) apt-get install tomcat7

### Database

Currently SHOGun also needs a database to work with. The easiest setup is to use a file-based H2-database. You only need to configure
the path to store the database contents in the file `src/main/webapp/WEB-INF/spring/db-config.xml` in a clone of the SHOGun repository:

    <property name="url" value="jdbc:h2:file:/absolute/path/to/db-file" />

The file will be created for you on initialisation.

If instead you want to go with  [PostgreSQL](http://postgresql.org/), here is some advice:

    $ (sudo) apt-get install postgresql

Also create a postgresql user and a database only for shogun. Execute the following SQLs inside of `psql`:

    CREATE USER shogun SUPERUSER PASSWORD 'shogun';
    CREATE DATABASE shogun OWNER shogun;

Please make sure that the user `shogun` can log in. See the PostgreSQL documentation for ways to achieve this.

Next, in a clone of the SHOGun repository, configure SHOGun to use the database we just created. Open the file `src/main/webapp/WEB-INF/spring/db-config.xml`
and find the `<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">`-tag. Change it to look like this:

    <!-- database connection via JDBC driver -->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName"><value>org.postgresql.Driver</value></property>
        <property name="url"><value>jdbc:postgresql://localhost/shogun</value></property>
        <property name="username"><value>shogun</value></property>
        <property name="password"><value>shogun</value></property>
    </bean>

In any case, make sure the `hibernate.dialect` is configured accordingly (also in the file `db-config.xml`).

### Packaging SHOGun

Now, let's run the maven `package`-phase. In the root of you clone (where the file `pom.xml` is located) issue:

    mvn package

This will download the dependencies of SHOGun and will eventually create a `war`-file that we can deploy on our tomcat. So, when you see the lines 

    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESSFUL
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 4 seconds
    [INFO] Finished at: Mon Oct 01 10:32:22 CEST 2012
    [INFO] Final Memory: 17M/42M
    [INFO] ------------------------------------------------------------------------ 

on your terminal, you are ready to copy the created `war`-file to tomcats `webapp`-folder:

    $ (sudo) cp target/SHOGun.war /var/lib/tomcat7/webapps/ 

### Checking visually

Now you can visit http://localhost:8080/SHOGun/ with you favorite web browser an you should be greeted with very basic login page. 
Try to log in as user `terrestris` with the password `xxxx`. On the top of the page you should now see a notce, that you are logged
in with the role 'ROLE_SUPERADMIN'.



