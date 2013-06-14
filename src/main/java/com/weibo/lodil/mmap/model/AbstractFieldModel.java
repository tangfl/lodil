package com.weibo.lodil.mmap.model;

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
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.weibo.lodil.mmap.impl.MappedFileChannel;


public abstract class AbstractFieldModel<T> implements FieldModel<T> {
	private final String fieldName;
	/* package local */final int fieldNumber;
	private Method setter;
	private Method getter;
	private String baseDirectory;

	protected AbstractFieldModel(String fieldName, int fieldNumber) {
		this.fieldName = fieldName;
		this.fieldNumber = fieldNumber;
	}

	public void setter(Method setter) {
		this.setter = setter;
	}

	public void getter(Method getter) {
		this.getter = getter;
	}

	public int fieldNumber() {
		return fieldNumber;
	}

	public Method setter() {
		return setter;
	}

	public Method getter() {
		return getter;
	}

	public String fieldName() {
		return fieldName;
	}

	public String titleFieldName() {
		return Character.toUpperCase(fieldName().charAt(0)) + fieldName().substring(1);
	}

	public String bcStoreType() {
		return storeType().getName().replace('.', '/');
	}

	public String bcLStoreType() {
		return "L" + bcStoreType() + ";";
	}

	public String bcModelType() {
		return getClass().getName().replace('.', '/');
	}

	public String bcLModelType() {
		return "L" + bcModelType() + ";";
	}

	public String bcFieldType() {
		return type().getName().replace('.', '/');
	}

	public String bcLStoredType() {
		return bcLFieldType();
	}

	public String bcLFieldType() {
		return "L" + bcFieldType() + ';';
	}

	public String bcLSetType() {
		return bcLFieldType();
	}

	public int bcFieldSize() {
		return 1;
	}

	public BCType bcType() {
		return BCType.Int;
	}

	public boolean virtualGetSet() {
		return false;
	}

	public boolean copySimpleValue() {
		return true;
	}

	public boolean isCallsNotEquals() {
		return false;
	}

	public boolean isCallsHashCode() {
		return isCallsNotEquals();
	}

	public void clear() {
	}

	public boolean isBufferStore() {
		return true;
	}

	public boolean isCompacting() {
		return false;
	}

	public static ByteBuffer acquireByteBuffer(MappedFileChannel mfc, int capacity) {
		return mfc == null ? ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder()) : mfc.acquire(capacity);
	}

	public void baseDirectory(String baseDirectory) throws IOException {
		this.baseDirectory = baseDirectory;
	}

	public String baseDirectory() {
		return baseDirectory;
	}

	public void flush() throws IOException {
	}
}
