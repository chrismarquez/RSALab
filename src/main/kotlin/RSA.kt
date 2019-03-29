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

        fun verify(signature: String, key: Key, asAscii: Boolean = true): String {
            val payload = signature.toBigInteger(16)
            val verification = payload.modPow(key.unique, key.shared)
            var str = verification.toString(16)
            if (str.length % 2 == 1) str = "0$str"
            return if (asAscii) str.hexToAscii() else str
        }

        fun loadFile(path: String): String {
            val url = this::class.java.getResource(path)
            return url.readText()
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