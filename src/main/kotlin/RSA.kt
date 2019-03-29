import java.math.BigInteger

class RSA (
    val publicKey: RSA.Key,
    val privateKey: RSA.Key
) {

    constructor(keys: Pair<Key, Key>): this(keys.first, keys.second)

    constructor(
        p: BigInteger,
        q: BigInteger,
        e: BigInteger
    ): this(genKeys(p, q, e))

    data class Key(
        val unique: BigInteger,
        val shared: BigInteger
    )

    companion object {
        fun encrypt(message: String, key: Key): String {
            val hexMessage = message.asciiToHex()
            val payload = hexMessage.toBigInteger(16)
            val cipher = payload.modPow(key.unique, key.shared)
            return cipher.toString(16)
        }

        fun decrypt(cipher: String, key: Key): String {
            val payload = cipher.toBigInteger(16)
            val message = payload.modPow(key.unique, key.shared)
            val hexMessage = message.toString(16)
            return hexMessage.hexToAscii()
        }

        fun sign(message: String, key: Key) = decrypt(message.asciiToHex(), key).asciiToHex()

        fun verify(signature: String, key: Key): String {
            val payload = signature.toBigInteger(16)
            val verification = payload.modPow(key.unique, key.shared)
            return verification.toString(16).hexToAscii()
        }

        fun loadCertificate(path: String) {
            val url = this::class.java.getResource(path)
            val text = url.readText()
            println(text)
        }

        private fun genKeys(p: BigInteger, q: BigInteger, public: BigInteger): Pair<Key, Key> {
            val totient = totientPrimes(p, q)
            val n = p * q
            val private = public.modInverse(totient)
            return Pair(Key(public, n), Key(private, n))
        }

        private fun totientPrimes(p: BigInteger, q: BigInteger) = (p - 1.toBigInteger()) * (q - 1.toBigInteger())
    }

    fun encrypt(message: String) = RSA.encrypt(message, publicKey)

    fun decrypt(cipher: String) = RSA.decrypt(cipher, privateKey)

    fun sign(message: String) = decrypt(message.asciiToHex()).asciiToHex()

    fun verify(signature: String) = RSA.verify(signature, publicKey)

}