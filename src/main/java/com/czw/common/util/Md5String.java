package com.czw.common.util;

import java.security.MessageDigest;

public class Md5String {
    
	public static final String Secret_MD5 = "MD5";
	public static final String Secret_SHA1 = "SHA-1";
    private MessageDigest m_md;
    private String secret_key;
    
    public Md5String()
    {
    	this(Secret_MD5);
    }

    public Md5String(String secretType)
    {
        super();
        try 
        {
            m_md = MessageDigest.getInstance(secretType);
        }
        catch (Exception e) 
        {
            System.err.println("MD5初始化失败");
            e.printStackTrace(System.err);
        }
    }
    
    public static String md5(String str)
    {
    	Md5String s = new Md5String(Secret_MD5);
    	s.addNormalObject(str);
    	return s.toString();
    }
    
    public static String sha1(String str)
    {
    	Md5String s = new Md5String(Secret_SHA1);
    	s.addNormalObject(str);
    	return s.toString();
    }
    
    public static String md5(byte[] b)
    {
    	Md5String s = new Md5String(Secret_MD5);
    	s.addNormalBytes(b);
    	return s.toString();
    }
    
    public static String sha1(byte[] b)
    {
    	Md5String s = new Md5String(Secret_SHA1);
    	s.addNormalBytes(b);
    	return s.toString();
    }
    
    public Md5String setSecretKey(String secretKey)
    {
        this.secret_key = secretKey;
        return this;
    }
    
    public void reset()
    {
        m_md.reset();
    }
    
    public Md5String addSecretBytes(byte[] b)
    {
    	if(b != null && b.length > 0)
    		m_md.update(b);
    	return this;
    }
    
    public Md5String addSecretString(String secr)
    {
        if(secr != null)
            m_md.update(((this.secret_key==null?"|":this.secret_key+"|")+secr).toLowerCase().getBytes());
        else
            m_md.update((this.secret_key==null?"|":this.secret_key+"|").getBytes());
        return this;
    }
    
    public Md5String addSecretStringNoCase(String secr)
    {
        if(secr != null)
            m_md.update(((this.secret_key==null?"|":this.secret_key+"|")+secr).getBytes());
        else
            m_md.update((this.secret_key==null?"|":this.secret_key+"|").getBytes());
        return this;
    }
    
    public Md5String addSecret(Object secr)
    {
        this.addSecretString(secr != null ? secr.toString() : null);
        return this;
    }
    
    public Md5String addSecretInt(int secr)
    {
        this.addSecretString(String.valueOf(secr));
        return this;
    }
    
    public Md5String addSecretLong(long secr)
    {
        this.addSecretString(String.valueOf(secr));
        return this;
    }
    
    public Md5String addNormalObject(Object secr)
    {
        if(secr != null && secr.toString() != null)
            m_md.update(secr.toString().getBytes());
        return this;
    }
    
    public Md5String addNormalInt(int secr)
    {
        m_md.update(String.valueOf(secr).getBytes());
        return this;
    }
    
    public Md5String addNormalLong(long secr)
    {
        m_md.update(String.valueOf(secr).getBytes());
        return this;
    }
    
    public Md5String addNormalBytes(byte[] b)
    {
        m_md.update(b);
        return this;
    }
    
    public String toString()
    {
        return this.md5HexString();
    }
    
    public String toHexString(int maxlength)
    {
        return this.pickString(this.md5HexString(), maxlength);
    }
    
    public String toOctalString(int maxlength)
    {
        return this.pickString(this.md5OctalString(), maxlength);
    }
    
    
    private String md5HexString()
    {
        byte[] key = m_md.digest();   
        
        StringBuffer strbuf = new StringBuffer();
        String temp = "";
        for (int i = 0; i < key.length; i++)
        {
            temp = Integer.toHexString((int)0xFF & key[i]).toUpperCase();
            if(temp.length() == 1)
                strbuf.append("0");
            strbuf.append(temp.toUpperCase());
        }
        return strbuf.toString();
    }
    
    private String md5OctalString()
    {
        byte[] key = m_md.digest();   
        
        StringBuffer strbuf = new StringBuffer();
        String temp = "";
        for (int i = 0; i < key.length; i++)
        {
            temp = Integer.toOctalString((int)0xFF & key[i]).toUpperCase();
            strbuf.append(temp.toUpperCase());
        }
        return strbuf.toString();
    }
    
    
    private String pickString(String str, int num)
    {
        if(str != null && num > 0)
        {
        	if(num <= 0 || str.length() == num)
        		return str;
        	else if(str.length() > num)
        		return str.substring(str.length() - num);
        	else
        	{
        		while(str.length() < num)
        			str = "0" + str;
        	}
        }
        return str;
    }

}
