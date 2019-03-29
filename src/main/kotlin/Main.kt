
lateinit var cryptex: RSA

fun main() {
    val tasks = arrayOf(task1, task2, task3, task4, task5, task6)
    var taskNumber = 1
    tasks.forEach {
        println("\n ==============  Task ${taskNumber++} ================== \n")
        it()
    }
}

val task1 =  {
    val p = "F7E75FDC469067FFDC4E847C51F452DF".toBigInteger(16)
    val q = "E85CED54AF57E53E092113E62F436F4F".toBigInteger(16)
    val e = "0D88C3".toBigInteger(16)
    cryptex = RSA(p, q, e)
    println(cryptex.privateKey)
}

val task2 = {
    val n = "DCBFFE3E51F62E09CE7032E2677A78946A849DC4CDDE3A4D0CB81629242FB1A5".toBigInteger(16)
    val e = "010001".toBigInteger(16)
    val key = RSA.Key(e, n)
    val message = "A top secret!".asciiToHex()
    val encoded = RSA.encrypt(message, key)
    println("Encoded: $encoded")
}


val task3 = {
    val n = "DCBFFE3E51F62E09CE7032E2677A78946A849DC4CDDE3A4D0CB81629242FB1A5".toBigInteger(16)
    val d = "74D806F9F3A62BAE331FFE3F0A68AFE35B3D2E4794148AACBC26AA381CD7D30D".toBigInteger(16)
    val cipher = "8C0F971DF2F3672B28811407E2DABBE1DA0FEBBBDFC7DCB67396567EA1E2493F"
    val key = RSA.Key(d, n)
    val message = RSA.decrypt(cipher, key)
    println("Decoded: $message")
}

val task4 = {
    val text = "I owe you \$2000."
    val alternateText = "I owe you \$3000."
    val textSignature = cryptex.sign(text)
    val alternateSignature = cryptex.sign(alternateText)
    println("Original: $textSignature")
    println("Alternate: $alternateSignature")
}

val task5 = {
    val text = "Launch a missile."
    val signature = "643D6F34902D9C7EC90CB0B2BCA36C47FA37165C0005CAB026C0542CBDB6802F"
    val n = "AE1CD4DC432798D933779FBD46C6E1247F0CF1233595113AA51B450F18116115".toBigInteger(16)
    val e = "010001".toBigInteger(16)
    val key = RSA.Key(e, n)
    var verification = RSA.verify(signature, key)
    println("Is original text verified: ${text == verification}")
    val alternateSignature = "643D6F34902D9C7EC90CB0B2BCA36C47FA37165C0005CAB026C0542CBDB6803F"
    verification = RSA.verify(alternateSignature, key)
    println("Is altered text verified: ${text == verification}")
}


val task6 = {
    val modulus = RSA.loadFile("publicKey.txt").toBigInteger(16)
    val exponent = RSA.loadFile("exponent.txt").toBigInteger(16)
    val publicKey = RSA.Key(exponent, modulus)
    val signature = RSA.loadFile("Signature1.txt")
    val payload = RSA.verify(signature, publicKey, asAscii = false)
    val hashValue = payload.substring(payload.length - 64 until payload.length)
    val realHash = RSA.loadFile("c0_body_hash.txt")
    println("Original Hash:   $realHash")
    println("Calculated Hash: $hashValue")
    println("Is verification successful: ${realHash == hashValue}")
}