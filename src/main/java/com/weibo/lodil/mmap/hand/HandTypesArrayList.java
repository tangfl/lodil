package com.weibo.lodil.mmap.hand;

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
import java.lang.annotation.ElementType;

import com.weibo.lodil.mmap.HugeArrayBuilder;
import com.weibo.lodil.mmap.api.HugeAllocation;
import com.weibo.lodil.mmap.impl.AbstractHugeArrayList;
import com.weibo.lodil.mmap.impl.MappedFileChannel;
import com.weibo.lodil.mmap.model.Enum8FieldModel;
import com.weibo.lodil.mmap.model.Enumerated16FieldModel;

@SuppressWarnings("rawtypes")
public class HandTypesArrayList extends AbstractHugeArrayList<HandTypes, HandTypesAllocation, HandTypesListElement> {
	final Enum8FieldModel<ElementType> elementTypeFieldModel = new Enum8FieldModel<ElementType>("elementType", 10,
			ElementType.class, ElementType.values());
	final Enumerated16FieldModel<String> stringEnumerated16FieldModel = new Enumerated16FieldModel<String>("text", 11,
			String.class);

	public HandTypesArrayList(final HugeArrayBuilder hab) throws IOException {
		super(hab);
		elementTypeFieldModel.baseDirectory(hab.baseDirectory());
		stringEnumerated16FieldModel.baseDirectory(hab.baseDirectory());
	}

	@Override
	protected HandTypesAllocation createAllocation(final MappedFileChannel mfc) {
		return new HandTypesAllocation(allocationSize, mfc);
	}

	@Override
	protected HandTypesListElement createElement(final long n) {
		return new HandTypesListElement(this, n);
	}

	@Override
	protected HandTypes createImpl() {
		return new HandTypesValueImpl();
	}

	@Override
	protected void compactStart() {
		stringEnumerated16FieldModel.compactStart();
	}

	protected void compactOnAllocation0(final HugeAllocation allocation, final long thisSize) {
		compactOnAllocation((HandTypesAllocation) allocation, thisSize);
	}

	@Override
	protected void compactOnAllocation(final HandTypesAllocation allocation, final long thisSize) {
		stringEnumerated16FieldModel.compactScan(allocation.m_string, thisSize);
	}

	@Override
	protected void compactEnd() {
		stringEnumerated16FieldModel.compactEnd();
	}

	@Override
	public void clear() {
		super.clear();
		elementTypeFieldModel.clear();
		stringEnumerated16FieldModel.clear();
	}
}
