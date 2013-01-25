/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory.support;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.*;
import org.springframework.beans.factory.xml.*;
import org.springframework.core.io.*;
import org.springframework.tests.sample.beans.*;

/**
 * Tests the use of lookup-method
 */
public class LookupMethodTests {

	private DefaultListableBeanFactory beanFactory;

	@Before
	public void before() throws Exception {
		ClassPathResource resource = new ClassPathResource("/org/springframework/beans/factory/xml/lookupMethodBeanTests.xml", getClass());
		this.beanFactory = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this.beanFactory);
		reader.loadBeanDefinitions(resource);
	}

	/**
	 * lookup method's bean has no constructor arguments
	 */
	@Test
	public void testWithoutConstructorArg() {
		AbstractBean bean = this.beanFactory.getBean(AbstractBean.class);
		assertNotNull(bean);
		Object expected = bean.get();
		assertThat(expected, instanceOf(TestBean.class));
	}

	/**
	 * Creates a new instance of {@link TestBean} using the constructor which takes a single {@code String}
	 */
	@Test
	public void testWithOneConstructorArg() {
		AbstractBean bean = this.beanFactory.getBean(AbstractBean.class);
		assertNotNull(bean);
		TestBean expected = bean.getOneArgument("haha");
		assertThat(expected, instanceOf(TestBean.class));
		assertThat("haha", equalTo(expected.getName()));
	}

	/**
	 * Creates a new instance of {@link TestBean} using the constructor which takes a {@code String} and an {@code int}
	 */
	@Test
	public void testWithTwoConstructorArg() {
		AbstractBean bean = this.beanFactory.getBean(AbstractBean.class);
		assertNotNull(bean);
		TestBean expected = bean.getTwoArguments("haha", 72);
		assertThat(expected, instanceOf(TestBean.class));
		assertThat("haha", equalTo(expected.getName()));
		assertThat(72, equalTo(expected.getAge()));
	}

	/**
	 *  {@link TestBean} doesn't have a constructor that takes a {@code String} and two {@code int}'s
	 */
	@Test
	public void testWithThreeArgsShouldFail() {
		AbstractBean bean = this.beanFactory.getBean(AbstractBean.class);
		assertNotNull(bean);
		try {
			bean.getThreeArguments("name", 1, 2);
			fail("TestBean does not have a three arg constructor so this should not have worked");
		} catch (AbstractMethodError expected) {
		}
	}
}

/**
 * A simple bean used for testing <code>lookup-method</code> constructors.
 *
 * The actual test class which uses this bean is {@link LookupMethodTests}
 *
 */
abstract class AbstractBean {

	public abstract TestBean get();
	public abstract TestBean getOneArgument(String name);
	public abstract TestBean getTwoArguments(String name, int age);
	public abstract TestBean getThreeArguments(String name, int age, int anotherArg);

}