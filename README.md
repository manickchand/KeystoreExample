# KeystoreExample
Exemplo de armazenamento de chaves com Keystore Android

A classe Crypto criptografa e decriptografa strings com o algoritimo AES.

![alt tag](https://github.com/manickchand/KeystoreExample/blob/master/keystore_example.gif)

### Criptografia

```bash
crypto.encryptText(yourAlias, yourTextToEncrypt)
```
Retorno - JSONObject
```bash
{
    "iv": strIV,
    "encrypted": "strDataEncrypted"
}

```

### Decriptografia

```bash
crypto.decryptText(yourAlias, yourTextToDecrypt, ivFromTextToDecrypt)
```
Retorno - String (Texto decriptado)
