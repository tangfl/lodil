package com.weibo.lodil.mmap;

/*
 * Copyright 2011 Peter Lawrey
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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Ignore;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.ASMifierClassVisitor;

import com.weibo.lodil.mmap.hand.HandTypesAllocation;
import com.weibo.lodil.mmap.hand.HandTypesArrayList;
import com.weibo.lodil.mmap.hand.HandTypesListElement;
import com.weibo.lodil.mmap.hand.HandTypesValueImpl;

@SuppressWarnings("rawtypes")
public class ClassNodeTest {
	@Test
	@Ignore
	public void test() throws IOException {
		for (final Class clazz : new Class[] { HandTypesArrayList.class, HandTypesAllocation.class, HandTypesListElement.class,
				HandTypesValueImpl.class }) {
			// for (Class clazz : new Class[]{HandTypesArrayList.class}) {
			final ClassReader cr = new ClassReader(clazz.getName());
			final StringWriter sw = new StringWriter();
			final ASMifierClassVisitor cv = new ASMifierClassVisitor(new PrintWriter(sw));
			cr.accept(cv, 0);
			final String text = sw.toString();
			System.out.println(text.replaceAll("\"com/weibo/lodil/mmap/hand/HandTypes", "name + \"")
					.replaceAll("Lcom/weibo/lodil/mmap/hand/HandTypes", "L\" + name + \"")
					.replaceAll("\"com/weibo/lodil/mmap/HandTypes\"", "name")
					.replaceAll("Lcom/weibo/lodil/mmap/HandTypes;", "L\" + name + \";")
					.replaceAll("\"com/weibo/lodil/mmap/", "collections + \"")
					.replaceAll("Lcom/weibo/lodil/mmap/", "L\" + collections + \""));
		}
	}
}
