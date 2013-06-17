package com.weibo.lodil.mmap.wrap;

import java.nio.CharBuffer;

import com.weibo.lodil.mmap.api.HugeAllocation;
import com.weibo.lodil.mmap.impl.GenerateHugeArrays;
import com.weibo.lodil.mmap.impl.MappedFileChannel;
import com.weibo.lodil.mmap.model.Enumerated16FieldModel;

public class DictAllocation implements HugeAllocation {

	CharBuffer keyBuffer;
	// CharBuffer valueBuffer;

	public DictAllocation(final int size, final MappedFileChannel mfc) {
		keyBuffer = Enumerated16FieldModel.newArrayOfField(size, mfc);
		//valueBuffer = Enumerated16FieldModel.newArrayOfField(size, mfc);
		// valueBuffer = keyBuffer;
	}

	// TODO all set to null?
	public void clear() {

	}

	public void destroy() {
		GenerateHugeArrays.clean(keyBuffer);
		// GenerateHugeArrays.clean(valueBuffer);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		destroy();
	}

	@Override
	public String toString(){
		return this.getClass() + " keyBuffer:" + keyBuffer;
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

	}

}
