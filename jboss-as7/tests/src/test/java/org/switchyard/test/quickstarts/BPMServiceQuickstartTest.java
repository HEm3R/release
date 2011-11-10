/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.switchyard.test.quickstarts;

import org.custommonkey.xmlunit.XMLAssert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.test.ArquillianUtil;
import org.switchyard.test.mixins.HTTPMixIn;

/**
 *
 * @author Tomohisa Igarashi <tm.igarashi@gmail.com> (C) 2011 Red Hat Inc.
 */
@RunWith(Arquillian.class)
public class BPMServiceQuickstartTest {

    @Deployment(testable = false)
    public static JavaArchive createDeployment() {
        return ArquillianUtil.createJarQSDeployment("switchyard-quickstart-bpm-service");
    }

    @Test
    public void testProcessOrder() throws Exception {
    	HTTPMixIn httpMixIn = new HTTPMixIn();
    	httpMixIn.initialize();
    	try {
    		String response = httpMixIn.postString("http://localhost:18001/swydws/ProcessOrder", SOAP_REQUEST);
    		XMLAssert.assertXpathEvaluatesTo("PO-19839-XYZ", "//orderId", response);
    		XMLAssert.assertXpathEvaluatesTo("true", "//accepted", response);
    		XMLAssert.assertXpathEvaluatesTo("Thanks for your order, it has been shipped!", "//status", response);
    	} finally {
    		httpMixIn.uninitialize();
    	}
    }

    private static final String SOAP_REQUEST = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
    		"    <soap:Body>\n" +
    		"        <orders:submitOrder xmlns:orders=\"urn:switchyard-quickstart:bpm-service:1.0\">\n" +
    		"            <orderId>PO-19839-XYZ</orderId>\n" +
    		"            <itemId>cowbell</itemId>\n" +
    		"            <quantity>100</quantity>\n" +
    		"        </orders:submitOrder>\n" +
    		"    </soap:Body>\n" +
    		"</soap:Envelope>";    	
}