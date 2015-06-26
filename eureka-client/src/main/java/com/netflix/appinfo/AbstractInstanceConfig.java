/*
 * Copyright 2012 Netflix, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.netflix.appinfo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.net.UnknownHostException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;

import com.netflix.discovery.shared.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract instance info configuration with some defaults to get the users
 * started quickly.The users have to override only a few methods to register
 * their instance with eureka server.
 *
 * @author Karthik Ranganathan
 *
 */
public abstract class AbstractInstanceConfig implements EurekaInstanceConfig {
    private static final Logger logger = LoggerFactory
            .getLogger(AbstractInstanceConfig.class);

    private static final int LEASE_EXPIRATION_DURATION_SECONDS = 90;
    private static final int LEASE_RENEWAL_INTERVAL_SECONDS = 30;
    private static final boolean SECURE_PORT_ENABLED = false;
    private static final boolean NON_SECURE_PORT_ENABLED = true;
    private static final int NON_SECURE_PORT = 80;
    private static final int SECURE_PORT = 443;
    private static final boolean INSTANCE_ENABLED_ON_INIT = false;
    private static final Pair<String, String> hostInfo = getHostInfo();
    private DataCenterInfo info = new DataCenterInfo() {
        @Override
        public Name getName() {
            return Name.MyOwn;
        }
    };

    protected AbstractInstanceConfig() {

    }

    protected AbstractInstanceConfig(DataCenterInfo info) {
        this.info = info;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.netflix.appinfo.InstanceConfig#getAppname()
     */
    @Override
    public abstract String getAppname();

    /*
     * (non-Javadoc)
     *
     * @see com.netflix.appinfo.InstanceConfig#isInstanceEnabledOnit()
     */
    @Override
    public boolean isInstanceEnabledOnit() {
        return INSTANCE_ENABLED_ON_INIT;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.netflix.appinfo.InstanceConfig#getNonSecurePort()
     */
    @Override
    public int getNonSecurePort() {
        return NON_SECURE_PORT;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.netflix.appinfo.InstanceConfig#getSecurePort()
     */
    @Override
    public int getSecurePort() {
        return SECURE_PORT;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.netflix.appinfo.InstanceConfig#isNonSecurePortEnabled()
     */
    @Override
    public boolean isNonSecurePortEnabled() {
        return NON_SECURE_PORT_ENABLED;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.netflix.appinfo.InstanceConfig#getSecurePortEnabled()
     */
    @Override
    public boolean getSecurePortEnabled() {
        // TODO Auto-generated method stub
        return SECURE_PORT_ENABLED;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.netflix.appinfo.InstanceConfig#getLeaseRenewalIntervalInSeconds()
     */
    @Override
    public int getLeaseRenewalIntervalInSeconds() {
        return LEASE_RENEWAL_INTERVAL_SECONDS;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.netflix.appinfo.InstanceConfig#getLeaseExpirationDurationInSeconds()
     */
    @Override
    public int getLeaseExpirationDurationInSeconds() {
        return LEASE_EXPIRATION_DURATION_SECONDS;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.netflix.appinfo.InstanceConfig#getVirtualHostName()
     */
    @Override
    public String getVirtualHostName() {
        return (getHostName(false) + ":" + getNonSecurePort());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.netflix.appinfo.InstanceConfig#getSecureVirtualHostName()
     */
    @Override
    public String getSecureVirtualHostName() {
        return (getHostName(false) + ":" + getSecurePort());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.netflix.appinfo.InstanceConfig#getASGName()
     */
    @Override
    public String getASGName() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.netflix.appinfo.InstanceConfig#getHostName()
     */
    @Override
    public String getHostName(boolean refresh) {
        return hostInfo.second();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.netflix.appinfo.InstanceConfig#getMetadataMap()
     */
    @Override
    public Map<String, String> getMetadataMap() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.netflix.appinfo.InstanceConfig#getDataCenterInfo()
     */
    @Override
    public DataCenterInfo getDataCenterInfo() {
        // TODO Auto-generated method stub
        return info;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.netflix.appinfo.InstanceConfig#getIpAddress()
     */
    @Override
    public String getIpAddress() {
        return hostInfo.first();
    }

    private static Pair<String, String> getHostInfo() {
        Pair<String, String> pair = new Pair<String, String>("", "");
        try {
            pair.setFirst(AbstractInstanceConfig.getHostAddress());
            pair.setSecond(InetAddress.getLocalHost().getHostName());

        } catch (UnknownHostException e) {
            logger.error("Cannot get host info", e);
        }
        return pair;
    }

  
    
  /**
     * @Author: Arun Kalyanasundaram
     * Utility method to get IP Address by enumerating network interfaces.
     * @param - None - default to eth0 now
     * @return - The IP Address String.
     */
    public static String getHostAddress() throws UnknownHostException{
    
		try {
			//Default to eth0 for now, read this from properties
			String interfaceName = "eth0";
			NetworkInterface netint = null;
			Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
			for (NetworkInterface netintIter : Collections.list(nets))
			{
				if(interfaceName.equals(netintIter.getName())) {
					netint = netintIter;
					break;
				}
			}
			Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
			for (InetAddress inetAddress : Collections.list(inetAddresses)) {
				byte[] rawArr = inetAddress.getAddress();
				if (rawArr!= null && rawArr.length>0 && rawArr[0]>0)
					return inetAddress.getHostAddress();
			}
		} catch (Exception e) {
		}
		//Failed to find address for the given interface name return default Address
		return InetAddress.getLocalHost().getHostAddress();
			
    }  
}
