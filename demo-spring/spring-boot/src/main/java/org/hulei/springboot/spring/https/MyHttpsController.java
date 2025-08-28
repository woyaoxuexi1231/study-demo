package org.hulei.springboot.spring.https;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hulei
 * @since 2025/8/2 15:05
 */

@RequestMapping("/https")
@RestController
public class MyHttpsController {
    /*
    Keytool 是 Java 开发工具包 (JDK) 中提供的一个密钥和证书管理工具，用于管理密钥库 (keystore) 和证书。以下是 keytool 的全面使用指南。
      - 密钥库 (Keystore): 存储密钥和证书的安全数据库文件
      - 别名 (Alias): 密钥库中每个条目的唯一标识
      - 证书链 (Certificate Chain): 从终端证书到根证书的证书序列

    keytool -genkeypair -alias <别名> -keyalg <算法> -keysize <大小> -keystore <密钥库文件> -validity <天数>
    keytool -genkeypair -alias myserver -keyalg RSA -keysize 2048 -keystore server.keystore -validity 365

    生成密钥匙可能需要以下信息：
        ⭐ 密钥库密码（Keystore Password）
            用于保护整个密钥库文件（通常是 .jks或 .pkcs12格式）的安全。只有知道密码的人才能访问密钥库中的密钥和证书。
        ⭐ 名字与姓氏（First and Last Name，即 Common Name，CN）
            这是证书中最重要的字段之一，通常用于表示证书持有者的身份。在 HTTPS 证书中，这个字段应该匹配服务器的域名（例如 www.example.com）。
            如果用于个人身份认证，则填写个人姓名。
        ⭐ 组织单位名称（Organizational Unit，OU）
            表示证书持有者所在的组织部门，比如“研发部”、“IT 部”等。这是一个可选但常用的字段，用于更细粒度地标识证书持有者所属的部门。
        ⭐ 组织名称（Organization，O）
            表示证书持有者所在的公司或组织名称，比如“阿里巴巴”、“腾讯”等。这是证书中重要的身份标识字段。
        ⭐ 城市或地区名称（Locality，L）
            表示证书持有者所在的城市或地区，比如“北京”、“上海”等。用于标识证书持有者的地理位置。
        ⭐ 州或省份名称（State or Province，ST）
            表示证书持有者所在的州或省份，比如“北京”、“广东”等。与城市信息一起用于更准确地定位证书持有者的位置。
        ⭐ 国家代码（Country Code，C）
            使用两个字母的国家代码（ISO 3166 标准），比如中国是 CN，美国是 US。这是证书中用于标识国家的重要字段。
        ⭐ 密钥密码（Key Password，可选）
            用于保护该特定密钥的安全。如果省略或不设置，通常会默认使用密钥库密码作为该密钥的密码。

    方便生成可以使用非交互式直接创建  在执行命令的目录会生成一个 myclientkeystore.jks 的文件
    keytool -genkeypair -alias myclient -keyalg RSA -keysize 2048 -keystore myclientkeystore.jks -validity 365 -dname "CN=client.example.com, OU=Dev, O=Client Corp, L=Shanghai, ST=Shanghai, C=CN" -storepass 123456 -keypass 123456
    这个命令将会触发：
        1. 生成一对 RSA 密钥对（公钥和私钥）。
        2. 把这对密钥保存在一个 KeyStore 文件（myclientkeystore.jks） 中。
        3. 为公钥生成一个自签名证书（也就是不是 CA 签发的，而是你自己签的），并存在同一个 keystore 里。
        4. 这个证书绑定了一个“身份”信息（你用 -dname 指定的），用来描述这个证书代表谁。

    # 查看密钥库中的所有条目（包括证书和别名）
    keytool -list -v -keystore myclientkeystore.jks

    # 可以导出公钥
    keytool -export -alias myclient -keystore myclientkeystore.jks -file myclient.cer -storepass 123456

    🌐 HTTPS 的握手过程简化图解
        当你访问一个 HTTPS 接口时（比如 https://localhost:8443/hello）：
        🔐 TLS 握手阶段：
          1. 客户端（比如浏览器或 curl）向服务器发起请求：Client Hello。
          2. 服务器返回：Server Hello + 证书（含公钥）。
          3. 客户端拿到证书后：
             - 检查证书是否合法（是不是 CA 签的）；
             - 然后用证书中的公钥加密一个随机生成的对称密钥（session key）；
          4. 服务器使用自己的 私钥 解密这个 session key；
          5. 双方后续用这个 对称密钥（AES 等）加密通信数据。

        ⚠️ 所以：
          - 公钥是在握手过程中用来加密 session key 的；
          - 你作为开发者不会看到这个细节，浏览器或 HTTP 客户端（如 Postman、curl、Java）会自动完成；
          - 如果证书是自签名的，你会在浏览器里看到“不被信任”的警告。
     

     导入证书：

     1.导出证书为 .cer 文件
     keytool.exe -exportcert -alias myclient -file mycert.cer -keystore myclientkeystore.jks

     2.将 mycert.cer 导入到 JDK 的 cacerts 信任库中
     keytool.exe -importcert -alias myclient -file mycert.cer -keystore security\cacerts -storepass changeit
     */
}
