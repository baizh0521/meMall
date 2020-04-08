package com.kingyee.me.common.security;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * FileString
 * 解决读取文件顺序的时候，顺序为1.jpg,10.jpg,11.jpg的问题
 * @author nmy
 * @time 2017-03-30 19:03
 */
public final class FileString implements Comparable<FileString> {
	private final String fileName;
	private final int prefix_num;
	Pattern number = Pattern.compile("(\\d+)\\..*");

	public FileString(String fileName) {
		this.fileName = fileName;
		Matcher matcher = number.matcher(fileName);
		if (matcher.find()) {
			prefix_num = Integer.parseInt(matcher.group(1));
		} else {
			prefix_num = 0;
		}
	}

	@Override
	public int compareTo(FileString o) {
		return o.prefix_num > prefix_num ? -1 : o.prefix_num == prefix_num ? 0 : 1;
	}

	@Override
	public String toString() {
		return fileName;
	}
}