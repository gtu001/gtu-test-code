package gtu.xml.xstream.iisi;

import gtu.string.StringCompressUtil;
import tw.gov.moi.ae.jms.JmsMessageNew;

public class MQDecodeTest {

    public static void main(String[] a) throws Exception {
        MQDecodeTest test = new MQDecodeTest();
        XmlParserImpl xmlParserImpl = new XmlParserImpl();
        String msg2 = "H4sIAAAAAAAAAIWVuZKjVhSG83mKyalqdhBVGlWxSGIRSGIVZAiuEGLVBTWCaMqpYwd25Mjl1A79PuPlLQzdnp6eGZd9I/jPd06d+x/uZd52D0n1+FBU6UMIHi5F86AWjQ6aJkyAAbrFm7dv58XzqxIv9D0m8h5OYQTB4DhGERzOYvgc/URMfAPKWApbsMBJjiRn5IyZsDn6on+EALT6pgXFmMd7z/FX0isqbafa2D/rhXyWJw6CCKSPr5JNcY5+JX5Gflnzi8DEjnbYYZM9lRu3Pi5ijn4SP0NsUNT/ij0FJrSFYdmEbVqVY659+MpJhhmd/AyasmLQRDCtJ2Xx+3ff//nNr3/8/O2H33766/0PH378ZY6+jk98NBqcVLAfHR17Gaczbu1FewKqW9kuJvXp4TknzwH8j65egImuYVrBtO0XRVVWdZX3c/RFmuIlALF4BlGmp6WdFmDRwhuYo1/Jn+Y7GSVWMVh8nOyL8NxwUcPxAwOx3iQLmWoU/uNScXfwWHsviAjXl4ywW+2Sa6tp2M3su7WqXr27u6Yrn0Js16tYQm2Km7Xc7ytls+rojqrMvnaSCKjIfiYcdXgjG6IixH3KAuTI0kRc0iKQBzxNuXDm1CHh0hpi1mEnqIDPaTuLO+p0ijPOsPmkUYK6Kw7LpKJnt2iPd4om+phQnrgYHmhkfWQpDie4AzvMaMpfXnEnFNKhFrjVxjifUz6oRY0O0Co5m9EBGQeAyeuA8iJ8c7aSWIJRuOntlXmwzpGjWqTQryUOh5VKK11R8rCXNhhjeZlPHy6E3IA9LuqMJKslmqC2qNC1lF0ElGt5wDEdpYoceZNsuYtwrfeq7d7bs4F+xKyLkC93/lKpsesVbOVHy1zfd9urfkG8VlshrldqO39WOR6WuPJMc4JLvz5vOdFg+N6gna0Vgr0TMRj2qOnqJfdkaT3LUTTiNhXpNgA7RqAfFJo7Xort2cJ9jIMBVnYpj5IIJ5csUjIDAiWEA+Tu2qMQcqhZiyp+dBCnltsBibqZ3hODs1OJwFLXFMlaJqSjIdQo/rK6OiiDFoFQ5bmVrrJYOsfqLMqW9CmCfXjVZ4fljvNNzUWYyL83uZsnPhUnBkU4bXwxcD5fK4+0fQ7QUtHgLaICTLUT2gLGnVZrscG0TDb980o0GmF9x1soMsJW2+FQVKSYPbqVo8N1PTOOmezhg+QjbLqF5GHJsGZMpD1CNau9NpyGTohWZetnxtVmc1HshaBRBh2Yak2oke3tDmoW3DGt2G9y8tidWBmS6g3fYPahuhmdlMa+zTcXaXB8uMrO0I+lVRjFkiKTqnYOafRCpOnWyzou1eqLTrrmZpPfg51xAqyqiGlaUp64E82dm6E3Z40im1O3Ik6u5G2TaDxY795N98Prc/dmvJv+51/xN2SYXnxNBgAA";
        final String jmsMessageXML = StringCompressUtil.uncompress(msg2);
        System.out.println(jmsMessageXML);
        JmsMessageNew jmsMessageNew = (JmsMessageNew)xmlParserImpl.parseToObj(jmsMessageXML);
        String returnMessage = jmsMessageNew.getMessageXML();
        System.out.println(returnMessage);
        System.out.println("done...");
    }
}