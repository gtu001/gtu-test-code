const buf = Buffer.from('hello world', 'ascii');

// Prints: 68656c6c6f20776f726c64
console.log(buf.toString('hex'));

// Prints: aGVsbG8gd29ybGQ=
console.log(buf.toString('base64'));


/*

'ascii' - For 7-bit ASCII data only. This encoding is fast and will strip the high bit if set.

'utf8' - Multibyte encoded Unicode characters. Many web pages and other document formats use UTF-8.

'utf16le' - 2 or 4 bytes, little-endian encoded Unicode characters. Surrogate pairs (U+10000 to U+10FFFF) are supported.

'ucs2' - Alias of 'utf16le'.

'base64' - Base64 encoding. When creating a Buffer from a string, this encoding will also correctly accept "URL and Filename Safe Alphabet" as specified in RFC4648, Section 5.

'latin1' - A way of encoding the Buffer into a one-byte encoded string (as defined by the IANA in RFC1345, page 63, to be the Latin-1 supplement block and C0/C1 control codes).

'binary' - Alias for 'latin1'.

'hex' - Encode each byte as two hexadecimal characters.

*/

const str = '\u00bd + \u00bc = \u00be';

//Prints: ½ + ¼ = ¾: 9 characters, 12 bytes
console.log(`${str}: ${str.length} characters, ` +
         `${Buffer.byteLength(str, 'utf8')} bytes`);
