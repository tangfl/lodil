package com.weibo.lodil.mmap.wrap;

import java.nio.CharBuffer;

import com.weibo.lodil.mmap.api.HugeAllocation;
import com.weibo.lodil.mmap.impl.GenerateHugeArrays;
import com.weibo.lodil.mmap.impl.MappedFileChannel;
import com.weibo.lodil.mmap.model.Enumerated16FieldModel;

public class DictAllocation implements HugeAllocation {

	CharBuffer m_string;

	public DictAllocation(final int size, final MappedFileChannel mfc) {
		m_string = Enumerated16FieldModel.newArrayOfField(size, mfc);
	}

	// TODO all set to null?
	public void clear() {

	}

	public void destroy() {
		GenerateHugeArrays.clean(m_string);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		destroy();
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

	}

}
