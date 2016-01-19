package com.ag.common.url;

public class AGUrlUtil {

	/**
	 * 识别是否为网址
	 * @param message
	 * @return
	 */
	public static String getLink(String message){

		char[] cha;
		int end = 0;
		String link = null;
		
		int index = message.indexOf("http://");
		boolean begin = message.startsWith("http://");
		if (!begin){
			index = message.indexOf("https://");
			begin = message.startsWith("https://");
		}
		if (!begin){
			index = message.indexOf("www.");
			begin = message.startsWith("www.");
		}
		if (!begin){
			index = message.indexOf("mail.");
			begin = message.startsWith("mail.");
		}
		if (!begin){
			index = message.indexOf("bbs.");
			begin = message.startsWith("bbs.");
		}
		if (begin){
			// 若存在，以此处开始，检查每一个字符是否为允许的字符，直到中文或者空格以及其他字符出现截止
			for(int i = index; i < message.length(); i++){
				cha = message.substring(index, index+1).toCharArray();
				if(cha[0]+0 == 13 || cha[0]+0 == 10 || (cha[0]+0 >= 33 && cha[0]+0 <= 126))
					end = i;
				else
					break;
			}
			// 初步得出网址
			link = message.substring(index,end+1);
			// 如果该网址不是以“http://”开头，则添加上去，否则webview不识别
			if(link.indexOf("http://") == -1 && link.indexOf("https://") == -1)
				link = "http://" + link;
		}
		return link;
	}
	
}
