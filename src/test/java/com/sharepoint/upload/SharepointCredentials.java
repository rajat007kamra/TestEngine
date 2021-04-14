package com.sharepoint.upload;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class SharepointCredentials {
	final static Logger logger = Logger.getLogger(SharepointCredentials.class);
	private InputStream inputStream;
    private Properties properties;

    public Properties getPropValues() throws IOException {
        try {
            properties = new Properties();
            String propFileName = "application.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if(inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("property file: " + propFileName + " not found in the classpath.");
            }
        } catch (IOException e) {
            logger.info("io exception: " + e);
            throw new IOException("error");
        } finally {
            inputStream.close();
        }
        return properties;
    }

    public static String getClientId() throws IOException{
        try {
            Properties propValues = new SharepointCredentials().getPropValues();
            String clientId = propValues.getProperty("client.id");
            return clientId;
        } catch (IOException ex) {
            throw new IOException("client id not found");
        }
    }

    public static String getClientSecret() throws IOException {
        try {
            Properties propValues = new SharepointCredentials().getPropValues();
            String clientSecret = propValues.getProperty("client.secret");
            return clientSecret;
        } catch (IOException ex) {
            throw new IOException("client secret value not found");
        }
    }

    public static String getTenantId() throws IOException {
        try {
            Properties propValues = new SharepointCredentials().getPropValues();
            String tenantId = propValues.getProperty("tenant.id");
            return tenantId;
        } catch (IOException ex) {
            throw new IOException("tenant id value not found");
        }
    }


    public static List<String> getScopeList() throws IOException {
        try {
            Properties propValues = new SharepointCredentials().getPropValues();
            String[] scopeList = propValues.getProperty("scope").split(",");
            return Arrays.asList(scopeList);
        } catch (IOException ex) {
            throw new IOException("scopes value not found");
        }
    }

    public static String getUsername() throws IOException {
        try {
            Properties propValues = new SharepointCredentials().getPropValues();
            String userName = propValues.getProperty("user.name");
            return userName;
        } catch (IOException ex) {
            throw new IOException("user name value not found");
        }
    }

    public static String getPassword() throws IOException {
        try {
            Properties propValues = new SharepointCredentials().getPropValues();
            String userName = propValues.getProperty("user.password");
            return userName;
        } catch (IOException ex) {
            throw new IOException("user name value not found");
        }
    }
}