/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.solace.labs.spring.boot.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.solacesystems.jcsmp.JCSMPChannelProperties;
import com.solacesystems.jcsmp.JCSMPProperties;
import com.solacesystems.jcsmp.SpringJCSMPFactory;

@Configuration
@AutoConfigureBefore(JmsAutoConfiguration.class)
@ConditionalOnClass({JCSMPProperties.class})
@ConditionalOnMissingBean(SpringJCSMPFactory.class)
@EnableConfigurationProperties(SolaceJavaProperties.class)
public class SolaceJavaAutoConfiguration {

    @Autowired
	private SolaceJavaProperties properties;

	@Bean
	public SpringJCSMPFactory connectionFactory() {
	    
	
        JCSMPProperties jcsmpProps = new JCSMPProperties();
        
        jcsmpProps.setProperty(JCSMPProperties.HOST, properties.getHost());
        jcsmpProps.setProperty(JCSMPProperties.VPN_NAME, properties.getMsgVpn());
        jcsmpProps.setProperty(JCSMPProperties.USERNAME, properties.getClientUsername());
        jcsmpProps.setProperty(JCSMPProperties.PASSWORD, properties.getClientPassword());
        if ((properties.getClientName() != null) && (!properties.getClientName().isEmpty())) {
            jcsmpProps.setProperty(JCSMPProperties.CLIENT_NAME, properties.getClientName());
        }
        // Channel Properties
        JCSMPChannelProperties cp = (JCSMPChannelProperties) jcsmpProps
                .getProperty(JCSMPProperties.CLIENT_CHANNEL_PROPERTIES);
        cp.setConnectRetries(properties.getConnectRetries());
        cp.setReconnectRetries(properties.getReconnectRetries());
        cp.setConnectRetriesPerHost(properties.getConnectRetriesPerHost());
        cp.setReconnectRetryWaitInMillis(properties.getReconnectRetryWaitInMillis());
        
        SpringJCSMPFactory cf = new SpringJCSMPFactory(jcsmpProps);
        
	    return cf;
	}

}