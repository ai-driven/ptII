package edu.umich.eecs.april.tag;

/*
This file is a modified version of a file by the same name in the
april package, written by Associate Professor Edwin Olson (ebolson@umich.edu),
EECS, University of Michigan. This file is part of a modified subset of the
april package created by Professor Edward A. Lee (eal@eecs.berkeley.edu) that
removes all dependencies on external libraries and graphical classes
and renames the packages to follow Java naming conventions.
The original files were retrieved on July 28, 2015, from the GIT repository
at git://april.eecs.umich.edu/home/git/april.git.  The Java implementation
AprilTags is described here: https://april.eecs.umich.edu/wiki/index.php/AprilTags-Java.
The April project is described here: https://april.eecs.umich.edu/.

The original author (Edwin Olson) has stipulated the following license:

This file is licensed under the terms of the GPLv2 or successor.

Alternative licenses use may be available by contacting
ebolson@umich.edu.

 */

/** Tag family with 2320 distinct codes.
    bits: 36,  minimum hamming: 10,  minimum complexity: 10

    Max bits corrected       False positive rate
            0                  0.00000338 %
            1                  0.00012491 %
            2                  0.00225182 %
            3                  0.02635678 %
            4                  0.22522267 %

    Generation time: 17115.640000 s

    Hamming distance between pairs of codes (accounting for rotation):

       0  0
       1  0
       2  0
       3  0
       4  0
       5  0
       6  0
       7  0
       8  0
       9  0
      10  70126
      11  58813
      12  270649
      13  180259
      14  617270
      15  297679
      16  649825
      17  205515
      18  249181
      19  50673
      20  33083
      21  4871
      22  1860
      23  184
      24  46
      25  6
      26  0
      27  0
      28  0
      29  0
      30  0
      31  0
      32  0
      33  0
      34  0
      35  0
      36  0
**/
public class Tag36h10 extends TagFamily
{
	public Tag36h10()
	{
		super(36, 10, new long[] { 0x1ca92a687L, 0x20521ac4cL, 0x27a3fb7d6L, 0x2b4cebd9bL, 0x3647bceeaL, 0x39f0ad4afL, 0x3d999da74L, 0x44eb7e5feL, 0x538f3fd12L, 0x5738302d7L, 0x65dbf19ebL, 0x70d6c2b3aL, 0x7f7a8424eL, 0x832374813L, 0x86cc64dd8L, 0x8a755539dL, 0x9570264ecL, 0x991916ab1L, 0xa06af763bL, 0xab65c878aL, 0xb2b7a9314L, 0xb660998d9L, 0xbdb27a463L, 0xcc563bb77L, 0xe24bdde15L, 0xed46aef64L, 0xf4988faeeL, 0x06e5417c7L, 0x158902edbL, 0x1cdae3a65L, 0x242cc45efL, 0x27d5b4bb4L, 0x2b7ea5179L, 0x32d085d03L, 0x3679762c8L, 0x3a226688dL, 0x3dcb56e52L, 0x48c627fa1L, 0x5769e96b5L, 0x6264ba804L, 0x660daadc9L, 0x6d5f8b953L, 0x74b16c4ddL, 0x7fac3d62cL, 0x91f8ef305L, 0x95a1df8caL, 0x994acfe8fL, 0xa09cb0a19L, 0xa445a0fdeL, 0xa7ee915a3L, 0xab9781b68L, 0xaf407212dL, 0xb69252cb7L, 0xc8df04990L, 0xd3d9d5adfL, 0xd782c60a4L, 0xf12158907L, 0x1d0c9ce43L, 0x20b58d408L, 0x2f594eb1cL, 0x3a541fc6bL, 0x454ef0dbaL, 0x53f2b24ceL, 0x629673be2L, 0x74e3258bbL, 0x8ad8c7b59L, 0x9d2579832L, 0xa8204a981L, 0xaf722b50bL, 0xb6c40c095L, 0xba6cfc65aL, 0xf15311ce5L, 0x0748b3f83L, 0x0af1a4548L, 0x0e9a94b0dL, 0x2be217935L, 0x3e2ec960eL, 0x4cd28ad22L, 0x507b7b2e7L, 0x54246b8acL, 0x57cd5be71L, 0x6a1a0db4aL, 0x6dc2fe10fL, 0x876190972L, 0x99ae4264bL, 0xabfaf4324L, 0xc9427714cL, 0xd09457cd6L, 0xd43d4829bL, 0xea32ea539L, 0xf52dbb688L, 0x161e2ea75L, 0x286ae074eL, 0x66a2d6963L, 0x8b3c3a315L, 0x8ee52a8daL, 0xa131dc5b3L, 0xe6bbb3352L, 0xf55f74a66L, 0x005a45bb5L, 0x07ac2673fL, 0x1da1c89ddL, 0x289c99b2cL, 0x3ae94b805L, 0x50deedaa3L, 0x5830ce62dL, 0x5bd9bebf2L, 0x632b9f77cL, 0x6e26708cbL, 0x841c12b69L, 0x92bfd427dL, 0x9668c4842L, 0x9dbaa53ccL, 0xb007570a5L, 0xb3b04766aL, 0xc25408d7eL, 0xea965ccf5L, 0xf93a1e409L, 0x0434ef558L, 0x1681a1231L, 0x1dd381dbbL, 0x302033a94L, 0x75aa0a833L, 0x92f18d65bL, 0x9a436e1e5L, 0xa1954ed6fL, 0xb78af100dL, 0xbb33e15d2L, 0xc62eb2721L, 0x0466a8936L, 0x0f6179a85L, 0x16b35a60fL, 0x589440de9L, 0x6738024fdL, 0x847f85325L, 0x9e1e17b88L, 0xacc1d929cL, 0xb06ac9861L, 0xd5042d213L, 0xfd468118aL, 0x0f9332e63L, 0x342c96815L, 0x37d586ddaL, 0x551d09c02L, 0x5c6eea78cL, 0x6017dad51L, 0x9354ffe17L, 0x9aa6e09a1L, 0xa94aa20b5L, 0xacf39267aL, 0xbb9753d8eL, 0xbf4044353L, 0x08730b6b7L, 0x1716ccdcbL, 0x22119df1aL, 0x3f5920d42L, 0x58f7b35a5L, 0x6b446527eL, 0x972fa97baL, 0x9e818a344L, 0xa5d36aeceL, 0xd1beaf40aL, 0xdcb980559L, 0xf65812dbcL, 0x139f95be4L, 0x6b761e65cL, 0x6f1f0ec21L, 0xa605242acL, 0xe43d1a4c1L, 0x29c6f1260L, 0x386ab2974L, 0x4e6054c12L, 0x8c984ae27L, 0x97931bf76L, 0x9ee4fcb00L, 0xd22221bc6L, 0xe46ed389fL, 0xebc0b4429L, 0x6f82813ddL, 0x732b719a2L, 0x9072f47caL, 0x941be4d8fL, 0x97c4d5354L, 0x9f16b5edeL, 0xb8b548741L, 0xd253dafa4L, 0xd5fccb569L, 0xd9a5bbb2eL, 0xe4a08cc7dL, 0x3c77156f5L, 0x7aaf0b90aL, 0xb53e1155aL, 0xb8e701b1fL, 0x05c2b9448L, 0x14667ab5cL, 0x47a39fc22L, 0x4ef5807acL, 0x64eb22a4aL, 0x982847b10L, 0xaa74f97e9L, 0xae1de9daeL, 0xb56fca938L, 0xf750b1112L, 0x1bea14ac4L, 0x1f9305089L, 0x233bf564eL, 0x31dfb6d62L, 0x3931978ecL, 0x52d02a14fL, 0x5a220acd9L, 0xb1f893751L, 0xfb2b5aab5L, 0x40b531854L, 0x5a53c40b7L, 0x8d90e917dL, 0x9139d9742L, 0x94e2c9d07L, 0xd6c3b04e1L, 0xe910621baL, 0xf40b33309L, 0x1152b6131L, 0x32432951eL, 0x3d3dfa66dL, 0x65804e5e4L, 0xab0a25383L, 0xb604f64d2L, 0xde474a449L, 0x11846f50fL, 0x6d03e854cL, 0x7455c90d6L, 0xab3bde761L, 0xdad013262L, 0xe973d4976L, 0x2b54bb150L, 0x9577f58a1L, 0x9920e5e66L, 0xb66868c8eL, 0xf4a05eea3L, 0x3dd326207L, 0x5b1aa902fL, 0x99529f244L, 0xb2f131aa7L, 0xd038b48cfL, 0xd3e1a4e94L, 0x24664cd82L, 0x36b2fea5bL, 0x95db6805dL, 0xa0d6391acL, 0xabd10a2fbL, 0x15f444a4cL, 0x2be9e6ceaL, 0x57d52b226L, 0x5f270bdb0L, 0xb6fd94828L, 0x879b19105L, 0xd476d0a2eL, 0xe6c382707L, 0xdbfa6a996L, 0x1689705e6L, 0x3b22d3f98L, 0x636527f0fL, 0x7d03ba772L, 0xee78d5a4dL, 0xbf165a32aL, 0xc2bf4a8efL, 0x517be89f2L, 0x67718ac90L, 0x6b1a7b255L, 0x726c5bddfL, 0xbb9f23143L, 0x1375abbbbL, 0x296b4de59L, 0x8893b745bL, 0xa9842a848L, 0xb827ebf5cL, 0x3840c894bL, 0x6b7deda11L, 0xbc02958ffL, 0x55ba04b51L, 0x76aa77f3eL, 0x9b43db8f0L, 0x9eeccbeb5L, 0xa295bc47aL, 0xb4e26e153L, 0xe476a2c54L, 0x6be1601cdL, 0x6f8a50792L, 0x97cca4709L, 0xbc66080bbL, 0x1093a056eL, 0x6fbc09b70L, 0xb8eed0ed4L, 0xcee473172L, 0x23120b625L, 0x5da111275L, 0xcf162c550L, 0xec8f68756L, 0xb5db0c4a9L, 0x2b2ad1127L, 0x536d2509eL, 0x9c9fec402L, 0xc1394fdb4L, 0x06c326b53L, 0x5e99af5cbL, 0xaf1e574b9L, 0xe6046cb44L, 0x661d49533L, 0x8e5f9d4aaL, 0xdb3b54dd3L, 0xe63625f22L, 0xe9df164e7L, 0x455e8f524L, 0x5b54317c2L, 0xbe258b389L, 0x54340a016L, 0x5b85eaba0L, 0x1284dcc1aL, 0x24d18e8f3L, 0x4d13e286aL, 0x8b4bd8a7fL, 0x215a5770cL, 0x46572d87aL, 0xc2c719ca4L, 0x4ddac77e2L, 0xd19c94796L, 0x2d1c0d7d3L, 0x9ae8384e9L, 0x9e9128aaeL, 0xca7c6cfeaL, 0x016282675L, 0xad985c97eL, 0x4af8bc195L, 0x9f580da26L, 0xa6a9ee5b0L, 0xbc9f9084eL, 0xd63e230b1L, 0xc4232a7b6L, 0xd66fdc48fL, 0xec657e72dL, 0xa364707a7L, 0xf79208c5aL, 0xde88a1f91L, 0x574f9ddf6L, 0x65f35f50aL, 0x69ce08eadL, 0x490f4ee9eL, 0xc9282b88dL, 0x752c4c7b8L, 0xb364429cdL, 0x8b53a7e34L, 0xbe90ccefaL, 0xb0507dfa2L, 0x00d525e90L, 0x5c549eecdL, 0xe3bf5c446L, 0x936c6d936L, 0x9747172d9L, 0xca843c39fL, 0xd57f0d4eeL, 0x2d5595f66L, 0xbfbb2462eL, 0x266727b98L, 0x7ac679429L, 0x26fc53732L, 0x656602d25L, 0x2eb1a6a78L, 0x4850392dbL, 0xe5b098af2L, 0xab534c280L, 0x9ce143f4aL, 0xf4b7cc9c2L, 0x035b8e0d6L, 0x871d5b08aL, 0x5b958930aL, 0x0b429a7faL, 0x54d8d431aL, 0x7d1b28291L, 0xa1e645021L, 0xb80da069dL, 0xeef3b5d28L, 0x263d3db6fL, 0x09592d503L, 0x4b9d86499L, 0x6c8df9886L, 0xa3740ef11L, 0xc4963b6dcL, 0x06da94672L, 0x53b64bf9bL, 0xb2deb559dL, 0xf116ab7b2L, 0x8ace1aa04L, 0x8ea8c43a7L, 0x6a4119dd3L, 0x99d54e8d4L, 0xc969833d5L, 0xf554c7911L, 0x3ade9e6b0L, 0x6e1bc3776L, 0x7916948c5L, 0xdbe7ee48cL, 0x79484dca3L, 0xf992e3a70L, 0x884f81b73L, 0xc68777d88L, 0x603ee6fdaL, 0x728b98cb3L, 0xb12701684L, 0xd5f21e414L, 0x058652f15L, 0x2dc8a6e8cL, 0x4767396efL, 0xb8dc549caL, 0xf36b5a61aL, 0x0d09ece7dL, 0xdda77175aL, 0x05e9c56d1L, 0x73e7a97c5L, 0xb21f9f9daL, 0xde3c9d2f4L, 0x69504ae32L, 0x77f40c546L, 0xed1217de6L, 0x3a1f88aedL, 0xe623a9a18L, 0x0aeec67a8L, 0xbea83aa19L, 0x92eeaf8bbL, 0xa5d08d12eL, 0x819a9bf38L, 0x473d4f6c6L, 0xb192431f5L, 0xa6c92b484L, 0x7046885b5L, 0xb9ab08cf7L, 0x782d94cd9L, 0xf158032faL, 0x077f5e976L, 0x12dda2281L, 0xe72417123L, 0x3056de487L, 0xe3de9931aL, 0xeb3079ea4L, 0xe4420bad6L, 0x439c2e4b6L, 0x47da4a615L, 0x0d7cfdda3L, 0x56afc5107L, 0xe978c5f8bL, 0x5aede1266L, 0xaf1b79719L, 0xf8b1b3239L, 0x75e8845dbL, 0xbf1b4b93fL, 0xfd5341b54L, 0xa2373b2d3L, 0x5967e672bL, 0xa2cc66e6dL, 0xb17028581L, 0xb54ad1f24L, 0xe91d22b84L, 0xde85c41f1L, 0x53d588e6fL, 0xe9e407afcL, 0xfc6272bb3L, 0xa8ca0629aL, 0xb86665d04L, 0x05a58fde9L, 0x1855b427eL, 0xaabb42946L, 0xe204ca78dL, 0x32897267bL, 0x0a78d7ae2L, 0x96536a598L, 0xbf2aea0a9L, 0x0c9bcd56cL, 0x81eb921eaL, 0x2732fe125L, 0x2eb69808dL, 0x61f3bd153L, 0x8ddf0168fL, 0x921d1d7eeL, 0x2bd48ca40L, 0x83ab154b8L, 0x5f436aee4L, 0x93dca0abcL, 0x26d75ad1eL, 0x872a1ba54L, 0x373a9f700L, 0x50d931f63L, 0x12d2f512cL, 0xc6efdbb59L, 0x88e99ed22L, 0x903b7f8acL, 0xa9da1210fL, 0xa2eba3d41L, 0xfe9cd615cL, 0xfb8911731L, 0x1cab3defcL, 0xe6289b02dL, 0x66d6a35b6L, 0xb0096a91aL, 0xc9afcc532L, 0x80aebe5acL, 0xd2f2e9768L, 0xcc67edb56L, 0xa51e37f35L, 0x6ac0eb6c3L, 0x6af2a4aa1L, 0xe76290ecbL, 0x37e738db9L, 0x72d9b11c5L, 0x76e613f46L, 0x4f073278bL, 0x0e1eea307L, 0xe9e8f9111L, 0x0793ee6f5L, 0x17304e15fL, 0x7a3361104L, 0x731339958L, 0x8daa6a511L, 0xa4037ef6bL, 0x210896f2fL, 0xafc535032L, 0xe3025a0f8L, 0x63e21ba5fL, 0x3ebb5b8c8L, 0xf9c6b06c3L, 0xca95ee37eL, 0x81f852bb4L, 0xd6895d823L, 0x7040cca75L, 0x4d66ec391L, 0x4a216e588L, 0x51d6c18ceL, 0x47711c319L, 0x6ae7f794cL, 0x4abe694d7L, 0xbc96f6f6eL, 0x57aa76cd2L, 0xf948f2648L, 0x31bcd1bc3L, 0x94c7b3f1dL, 0x32eef86acL, 0x668f8ff2eL, 0x6de170ab8L, 0x9341b93e2L, 0x974e1c163L, 0x73182af6dL, 0x5d85fb48bL, 0x78b257bdeL, 0x173d0eb29L, 0x5add785d1L, 0xaf6e83240L, 0xdce79166cL, 0x22716840bL, 0x7b408f1d9L, 0xde43a217eL, 0x36e10fb6eL, 0xea9a83ddfL, 0x4dcf50162L, 0x9aab07a8bL, 0x281364431L, 0x8392dd46eL, 0x0945e6aceL, 0x8d07b3a82L, 0xd012f1990L, 0xb7098acc7L, 0xc2fcfa16cL, 0x1e7c731a9L, 0xf16ea68eeL, 0x2fa69cb03L, 0x4cee1f92bL, 0x1e20cfda2L, 0x9e9d1ef4dL, 0xe83358a6dL, 0x59a873d48L, 0xa6842b671L, 0x6885bdbefL, 0x73e4014faL, 0x7b9954840L, 0x548157ffdL, 0x8853a8c5dL, 0xeb8874fe0L, 0xb25d4f257L, 0x36b447da5L, 0x71d87958fL, 0xeedd91553L, 0x4af23612aL, 0x278329eacL, 0x191121b76L, 0x6e691175dL, 0x0bd140329L, 0x6ed4532ceL, 0xd5e3c8ff4L, 0xe26c64033L, 0x494a2097bL, 0xf5e36d440L, 0xb7818d202L, 0xa63548c34L, 0x682f0bdfdL, 0x3d3c65c17L, 0x4c4399ae7L, 0x6b18e67ffL, 0x4778211a3L, 0x4089b2dd5L, 0x21edee850L, 0x739cede72L, 0x858dfc744L, 0x4bc5dba6cL, 0x21cbd3bdcL, 0xac83de313L, 0x6135f08daL, 0xd3a3a9f0bL, 0xeb2716099L, 0x40b88e413L, 0x992442a25L, 0xc639de695L, 0x683bcc7c7L, 0x6245fc74fL, 0x543766bd5L, 0x375356569L, 0xfa45b7a88L, 0x9f29b1207L, 0xba245457cL, 0x5b98e5ec9L, 0x204aca6b6L, 0xd52e9605bL, 0x504a40d28L, 0xab6e1695eL, 0xa26481ebbL, 0x9455ec341L, 0x2f05f98e9L, 0xead83365cL, 0xb928d8486L, 0x7b860de0bL, 0x964ef7da2L, 0x2422962b9L, 0x28f5ddfb2L, 0x8c5c63713L, 0x9068c6494L, 0x50aad5744L, 0x93e7cca30L, 0x9825e8b8fL, 0x3a8b4947dL, 0xfa06737b5L, 0xcb9c963e8L, 0x91a2bc332L, 0x284666b59L, 0xceb82a1c8L, 0xa2fe9f06aL, 0x06fa50365L, 0x19aa747faL, 0xa6e117dc2L, 0x2b3810910L, 0xff7e857b2L, 0x048b55c3eL, 0x975456ac2L, 0xad200ed37L, 0xe4d4d86efL, 0xb3ec62491L, 0x8cd465c4eL, 0x8a2be2d94L, 0x5a9f7d648L, 0x59e067a85L, 0xb5c35327eL, 0xc4ca8714eL, 0x0e927a04cL, 0xf71eac628L, 0x109354e62L, 0x1037b1a5bL, 0x26c27f893L, 0x3597fa385L, 0xee24b62efL, 0x4b31f921cL, 0x650244e5dL, 0xcfec64526L, 0xcd4bb0a21L, 0x648c56197L, 0xbd95056f8L, 0x983c8c183L, 0xddc662f22L, 0x5631bb980L, 0x1203f56f3L, 0x6a0c37549L, 0xa59baa8a4L, 0x6a23a5068L, 0xad609c354L, 0xf6f6d5e74L, 0x36bc95f79L, 0x424c92c62L, 0x3692abf50L, 0x0a4bc460dL, 0x1b4434b89L, 0x4eb31302dL, 0x9a3a891f9L, 0x93af8d5e7L, 0xef60bfa02L, 0x607a378d6L, 0x5a5a7d835L, 0x536c0f467L, 0x11f66a7feL, 0x74c7c43c5L, 0x66eae7c29L, 0xf5a785d2cL, 0xd3948a5c0L, 0xec1094aa4L, 0xfcad61c19L, 0x49ca7108aL, 0x77437f4b6L, 0x553083d4aL, 0x5c26c14cdL, 0x6eb4caceeL, 0xe8aded63cL, 0x132aa4b5cL, 0x57603a19eL, 0xee359dda3L, 0xd0f5ea330L, 0x46b0f0b1fL, 0xd47cbfc81L, 0x3ed1b37b0L, 0x4c8c752d8L, 0xac202044bL, 0x207f16128L, 0x53f5c3981L, 0x70e1a33a2L, 0xed8348baaL, 0x798f94a3eL, 0x8896c890eL, 0x521425a3fL, 0xc8329e9eaL, 0x41f238ba5L, 0x1093d3c81L, 0xcab628e90L, 0xa1b4bf356L, 0x92eee2fceL, 0x59d35b9afL, 0x2176e9f53L, 0x7c9abfb89L, 0xb06d107e9L, 0xe94c318d5L, 0x2f397ae30L, 0xd7e5a1a48L, 0x98c4abc47L, 0x574737c29L, 0xd7f5401b2L, 0x852b87bc6L, 0x1180fa957L, 0x501c63328L, 0xfd28c0d13L, 0xf764aa079L, 0xc8a6f8c5aL, 0x975b10240L, 0x6bd33e4c0L, 0xa1113e39cL, 0xabcfa8fb8L, 0x149ea1facL, 0xf6443556fL, 0x959da07e7L, 0x4721a2ac4L, 0x30bde0f15L, 0xc7c4fdef8L, 0xb7feb4465L, 0x56675073cL, 0x96f3f57b9L, 0x876f0386eL, 0x393f0a7e8L, 0x32b40ebd6L, 0x10a11346aL, 0xfb81f48aeL, 0x0a892877eL, 0x86f636e08L, 0x4fbc4d72bL, 0x561d5f314L, 0xca18e2835L, 0x7e6f519f5L, 0x8b94e7983L, 0x619adfaf3L, 0x4c9ddbbabL, 0xcb6a461f2L, 0x0f6e22456L, 0x858c9b401L, 0x92dc1b3b8L, 0x3a783615bL, 0xc74b66f67L, 0xea90891bcL, 0x31a829e4bL, 0x7bd38f505L, 0xb4476ea80L, 0x1af371feaL, 0x84894ff56L, 0x0537584dfL, 0xd4ebdd1d0L, 0xb43cc192bL, 0x76700d287L, 0xaaad9fa58L, 0xfa511710fL, 0x6e54699e5L, 0x6d73391aeL, 0x3c2f1fb49L, 0x4fbd96a75L, 0x252e6304bL, 0xe72826214L, 0x8fe6c9336L, 0x326397743L, 0x0e03bc524L, 0xdedac9594L, 0x04ff19096L, 0x409b4cdbbL, 0xb15921888L, 0xa259bcd6dL, 0x43c67f305L, 0x1dc65dcecL, 0x1730b4f85L, 0xf8a48f16aL, 0x57a30e743L, 0x05afd9839L, 0x682d5f3aeL, 0x0b694337eL, 0x758c7dacfL, 0x18fa1ae7dL, 0x5ba9b5984L, 0x32e1d45ddL, 0x672f05518L, 0x382ffc5b1L, 0x8f0b08f33L, 0xb4a4d9ff0L, 0x53ba0f980L, 0x2ac0751fbL, 0xab005de73L, 0x61ab7be9bL, 0x63078c9adL, 0x27659d148L, 0xc653c684fL, 0xecee05017L, 0x24378ce5eL, 0xd0f7e5bacL, 0x8b4bf4199L, 0xb7aa495fbL, 0xf3d6b78a5L, 0x98bab1024L, 0x6b4971fadL, 0x0723d6c89L, 0xa17071a75L, 0xd55a301f4L, 0x988de925bL, 0x7ca276f45L, 0x321b6e484L, 0x316149ed6L, 0xb8dba5bb9L, 0xaad4df3f4L, 0x178e204f5L, 0x95cd2e357L, 0x73fb8a733L, 0x84ca10c86L, 0x89498492dL, 0x5b9bdf383L, 0x66c58bb10L, 0xf153ac21eL, 0x7ca5d3b04L, 0x637a521c7L, 0xb5ed589c1L, 0xd0a3c644eL, 0x3d5d0754fL, 0xc6b901174L, 0x3e96fd3edL, 0x79c2fdf8cL, 0x6594ae371L, 0x3504fd77aL, 0x32b81dcc7L, 0x57b4f3e35L, 0x4c9808072L, 0xa06c10993L, 0xb059666afL, 0x72b69c034L, 0xe33ae836eL, 0xad6cadf0dL, 0x19573ace1L, 0xd562fd1e7L, 0x847804d9dL, 0x9e32f6734L, 0x2355c580fL, 0x2f177b8d6L, 0xd689ac650L, 0x071b70abcL, 0x254915730L, 0xc5934f949L, 0x134d59d09L, 0x2c731fb06L, 0xd90c6c5cbL, 0xcc3f9bca4L, 0xbf078980cL, 0x80838e95aL, 0x5ccd6f054L, 0x0378bae56L, 0x8d1ddb978L, 0x93b875cf4L, 0xe2ce90bc6L, 0xec291b91bL, 0xd0a11bdc1L, 0x751be7244L, 0x579fcd29eL, 0xf76e5ccb1L, 0xeb33da184L, 0xc0ac75b0fL, 0x15454fb33L, 0x8b92a411cL, 0x7da34b476L, 0x4f413d45eL, 0x10ec1dbeaL, 0x650739b93L, 0x315e08a31L, 0xd4fd5f1bdL, 0xb5058a126L, 0x20d5cb63bL, 0xc1598dfe7L, 0xa0a2a338dL, 0xe29af7686L, 0x83fd0cac9L, 0x14a8094d8L, 0x816e0afa3L, 0x499ef5d2cL, 0xbddbd0d95L, 0xa30839ca9L, 0x4fd33fb4cL, 0xef63557b7L, 0x535f06ab2L, 0x0d47d352eL, 0xa371e6dc4L, 0x8ac914b17L, 0x6ba9d69d7L, 0x096da89aaL, 0x65bbd5d14L, 0xd41d2c5c4L, 0x52a283583L, 0x4c1f56d26L, 0x86cd9984aL, 0x26cbcedc6L, 0x1aa0eaa03L, 0xb95d5ad2cL, 0xe2eb56b20L, 0xff49d9d5cL, 0xcce338378L, 0x330e9fac7L, 0xe2f53974aL, 0x668d1c6d5L, 0xeca0ba751L, 0x8d48ab5e6L, 0xd205e18cdL, 0x1c391633cL, 0xef5d02e5fL, 0xd12bb5f20L, 0x323215199L, 0x88f5b3ffcL, 0x931445f29L, 0xb893cb727L, 0x32851ecc0L, 0x80b44d81bL, 0x5aa48da98L, 0x46d1e1284L, 0x4c837ba14L, 0xeb22c26deL, 0xe51e9d246L, 0x8d03deee6L, 0x5af8e0909L, 0xbde9773a4L, 0xbf611cabfL, 0xd24ac96e7L, 0x9fe919318L, 0x50d0206a6L, 0xb43b9741cL, 0xba48d4fb3L, 0x6bccd7290L, 0x8bc6bfb9cL, 0xe5a036c9fL, 0xa80a2cfeeL, 0xc193655a7L, 0x7c8e5170dL, 0x6141edbbbL, 0x4d6b990dcL, 0xcc49b5702L, 0x2343fef58L, 0xd50cb593cL, 0x4248a60cdL, 0x901cfbd4cL, 0x64a4c8736L, 0x1b2dcbaeaL, 0xd691e5f4cL, 0xdf352a493L, 0x1991ac7daL, 0x4c4879f45L, 0x9b34aadeeL, 0x52bb3db0dL, 0x7b9a8c9d3L, 0xd7ce6e47eL, 0xec0b922d8L, 0x8079cab6bL, 0xabadc8899L, 0x0f57b93b7L, 0x05c4ef219L, 0xd7a438d49L, 0xf55ecca97L, 0xd07899f1dL, 0x260947d6cL, 0xffbd21ab6L, 0xd04ff923eL, 0x964b72033L, 0x31ac3fd7eL, 0xd2c52e2c4L, 0x799a640efL, 0x98dd061edL, 0x5cb2ab7b8L, 0x72f3881c8L, 0xe65ed1164L, 0x34fa0bd5bL, 0x64f9823cdL, 0x3797e1ac0L, 0x2fb8a4751L, 0x6f347342eL, 0x22dd7ea0aL, 0xb19b65e57L, 0x44fe83e8aL, 0x07732732eL, 0x64de20ed7L, 0x06c9ea834L, 0x8ce066650L, 0xc2a685ff0L, 0x64f19b01fL, 0x491ab8a88L, 0x41212fe5aL, 0x6f9916f3bL, 0x694f72e71L, 0xad7a5b35eL, 0xf62795292L, 0xc8cdc3d3aL, 0xfbc6b3518L, 0x67b631901L, 0x5b5ba79d5L, 0xf4fadebddL, 0xac7c802e7L, 0x385712d9dL, 0x64bd375b4L, 0xc9a11df70L, 0x88355bf31L, 0x606ffbb0aL, 0xbda93c2d5L, 0x7c5f94f0aL, 0x76fe26501L, 0x5d8b9153cL, 0x886bbb218L, 0xacee2fecaL, 0x2ad19a925L, 0x83b97855cL, 0xd36608312L, 0x8ac60dbc7L, 0x0885c8f58L, 0x8abbdf891L, 0xea1602271L, 0xad654fee1L, 0x6c461195eL, 0x5eeb1a327L, 0x18d743962L, 0x1fc7c55a5L, 0xaba749670L, 0x9c9a59c60L, 0x6e5bafc06L, 0x96977db12L, 0xa97b6ebfaL, 0x63d2d9da6L, 0xfab00cd60L, 0xd7bdf4632L, 0xf83878d59L, 0xb1c2c462eL, 0x14e5144a7L, 0xf4a909b28L, 0xe979a185bL, 0x908090a64L, 0x99eccd798L, 0x348780a96L, 0xfdc7ad169L, 0xa600c2e5bL, 0xb0968cd98L, 0x1a45ec098L, 0x99118c1b4L, 0x8afa5cd5aL, 0x1db7e655eL, 0x9f637e452L, 0x9568504e3L, 0x045b2a662L, 0xf2a1455a2L, 0x6c1ca9e75L, 0x30a4a4639L, 0xc6c2c1a30L, 0x87500b452L, 0x5e338bb2eL, 0xd9dd11dffL, 0x8c4b5d012L, 0x8191194e0L, 0xdd11db867L, 0xc67c151ceL, 0x5cb1a00e4L, 0x098b7a1c6L, 0x369f35cd4L, 0xca2190bdbL, 0x6e14bb3b9L, 0x8d5692f8cL, 0xca4b2f4f8L, 0x787f06877L, 0x8acbb8550L, 0x535f4b56aL, 0xf4caf7ecbL, 0xd4615b258L, 0x347ca7070L, 0x3c798c85dL, 0x460506465L, 0x870d0a5dcL, 0x6510b2464L, 0xd1dba5544L, 0xd57789a33L, 0xe2417c5baL, 0xb5ff8628cL, 0xa3bb22787L, 0xa16b64f34L, 0x421e81d3dL, 0x35b4596a7L, 0x8d7a2dd7eL, 0x50b2d83faL, 0x9ea87e7c2L, 0xd5055e752L, 0xf96aa9da5L, 0xb096e2a07L, 0x49970b44bL, 0x867fb1518L, 0x5d0f5dba2L, 0x1b191d11eL, 0x8e839bb8fL, 0x1cd4aca15L, 0x971ec5615L, 0x7d72a7ebdL, 0x8b1253bfbL, 0xe11de1d25L, 0x0a7566839L, 0xf4f3542e0L, 0x1ea791e32L, 0x32a84f759L, 0x646f1844eL, 0x42af26809L, 0x1f4b464ffL, 0xda684d2d9L, 0xd854f5fb9L, 0x4d4d3e91aL, 0x5af3ef4e2L, 0x8a1ef5ce7L, 0x2354febf3L, 0xb3c5a8944L, 0x98b62a144L, 0x9bdba0b4eL, 0x04aa99b42L, 0x8099ea151L, 0x2185463a3L, 0xb0a1ae997L, 0xe628d5770L, 0xb40b5ac89L, 0x27213b17dL, 0x4d21db5b5L, 0x10d0748f7L, 0x2276c7876L, 0xb98bee56dL, 0xbd1ca6ae8L, 0x824ab48faL, 0xc6f35ae62L, 0x3547a563cL, 0xf1fc0d824L, 0x58f55ed75L, 0xaa9d0de01L, 0x4719dde60L, 0xd5386b3ddL, 0x4d8d9f666L, 0xaee36013bL, 0xba4ee322fL, 0x898d2db4eL, 0x9fe364808L, 0xbb13e8045L, 0xbe346d43aL, 0xb4c9f886fL, 0xc9a6f53b8L, 0x0ed5a7b6fL, 0x2a1fac740L, 0xb8c134a59L, 0xb1f773993L, 0xc4d9d0025L, 0xca905bdcaL, 0x3150a39a7L, 0xe8329fad5L, 0xbd4f98059L, 0x3bc5cf6cdL, 0xc982fdd03L, 0x0a372de28L, 0x73fe2e35aL, 0x0b9f684ecL, 0xc543ff680L, 0x1bcf5f09aL, 0x51b2a8099L, 0xee53277c2L, 0x0b3835a6cL, 0xaed6765c1L, 0x92cfd64c8L, 0xd20c60ed2L, 0x59dbd9f51L, 0xb6acb694bL, 0x427dcd5fdL, 0x646336a75L, 0x8008dea4dL, 0x0af2bdc7cL, 0xb8a46478aL, 0xb02c535b6L, 0xc645d8631L, 0x044b4af3dL, 0xc9edfe6cbL, 0x32ac8ea2aL, 0x79266a23fL, 0xc2d902e93L, 0x6ae5cfbdbL, 0x2c66c633eL, 0xeb7a8a4e3L, 0xcb17281cfL, 0x7ca378680L, 0x7ac81509dL, 0xa59a05073L, 0xc9cb9f18dL, 0xb78100d29L, 0xfab49420aL, 0xd0a4e69c4L, 0xd6c33f722L, 0x68d21bff8L, 0x1fdad8ca3L, 0x2884d6968L, 0xb091ff264L, 0xeb5fb236fL, 0xa3d2a1839L, 0x527db0bc8L, 0x2dc68cd9fL, 0xe3f4ea98aL, 0xa629fe44fL, 0xb73bd7d66L, 0x2abfd7b6bL, 0x1b4056054L, 0xd6efaac28L, 0x0d13cc950L, 0xef84ead94L, 0x5b6ee0d50L, 0x0f4bec692L, 0xde1b98881L, 0x55ccccd31L, 0x086d9b84dL, 0x5ab736e3dL, 0x167d2f005L, 0x118ed1522L, 0x38bbdc903L, 0x39cd31ac2L, 0x31091bc51L, 0xd66a87d3fL, 0xafdade6d3L, 0x2bd1fe097L, 0x5cf545dd2L, 0x5e0af578eL, 0x6fe6dd4c9L, 0x862bc8fcaL, 0xcbce0b4c6L, 0x08b7fa8ddL, 0x3d108ae9fL, 0xfed2d914aL, 0xbab304bd8L, 0xdebe74f8dL, 0x1e857e3dcL, 0x570340581L, 0x114bbf4f5L, 0xa3cfc0566L, 0x4026cd686L, 0x266fb76cdL, 0xb715773bbL, 0x2fd2785fdL, 0x481b34cadL, 0x11c58d2baL, 0x3a5186f4dL, 0xda55ab71cL, 0xac887db92L, 0x9bd6d5592L, 0x45857d12aL, 0x8c862f0b9L, 0x870c88666L, 0x4a4f4901fL, 0x774a993d0L, 0xc9f16c81dL, 0xeb415e9efL, 0x307aa6302L, 0xa246f21eeL, 0x1a4f8a9c2L, 0x0cf09f9b4L, 0xdb30dbb49L, 0x3581be36fL, 0x6919a4318L, 0x8ee677afdL, 0x5944b9d59L, 0x8d5fe61aaL, 0x77c174b1dL, 0x5cff8fa10L, 0xc1ce82f48L, 0x7fbb18e65L, 0x0b6737103L, 0xe2d30a9b6L, 0x6481ff469L, 0x5834b4d26L, 0x3bba517d5L, 0xeee6e8080L, 0x5fe4fea5eL, 0xe84e94c8cL, 0xba2ad0a2aL, 0xa7f2aead0L, 0x63cecb46dL, 0x8943d7229L, 0x1d3878b2bL, 0xf2b4efe94L, 0xd9af1949dL, 0xbb5824d39L, 0xb8d8f5090L, 0xed5e19d08L, 0x60287437eL, 0x8fe6ae5c2L, 0x6c85ac058L, 0xb906be1b8L, 0xf9d423f65L, 0x6efed81d6L, 0x781b67fa2L, 0xe1dd437acL, 0x7a9201a8cL, 0xfb444c819L, 0xce75af959L, 0x86df6e72bL, 0x756695aa7L, 0xb7b2bddf2L, 0xf19a1b99eL, 0x9a5790e90L, 0x1d3b3eac0L, 0xa5c5d9d2bL, 0x152850218L, 0x025c4ba6eL, 0xd4a5f4bebL, 0x709cec10eL, 0x94ddbdb6cL, 0x9d1218277L, 0x6190ca34aL, 0x468ed6a3fL, 0x801bda52eL, 0x261b3f1a9L, 0x0b3494d9bL, 0x583e2d7e5L, 0x9407a80f2L, 0x58e902456L, 0x9108c2273L, 0x59778ff8cL, 0xd6ce05028L, 0x0286adc62L, 0x7ed3060dcL, 0x57b7e03edL, 0x3e3dce5c1L, 0x1bebc2295L, 0x014a17c9aL, 0xc7d90fbdaL, 0x8158ae35aL, 0x69d70a335L, 0xd3ef97931L, 0x5793efb7aL, 0xe6989ef43L, 0xcd15f0116L, 0xf9dbc6e25L, 0xda4a91117L, 0x054d0917aL, 0x60f2c3f15L, 0x7393b0a66L, 0x6630ed79bL, 0xed8589c60L, 0x7db37ab26L, 0xc4631e80aL, 0x1badaf501L, 0x9bdef764dL, 0xdd0949b4bL, 0x86f116771L, 0xacd7ea109L, 0x7cc9d2f6bL, 0x3f5598822L, 0x4ba5a8d0cL, 0x66e7f9c42L, 0x33127fb36L, 0x0c85ff976L, 0x9dbb32ddfL, 0x3d06c7a56L, 0xac07601ddL, 0x5fda3d7e9L, 0x40a47aef0L, 0x139928cd0L, 0x183ab75ebL, 0x9dd6d1f4bL, 0x954afec44L, 0x29953fe22L, 0xf947e49b1L, 0xa74266cb0L, 0x3bbb7fdabL, 0x8a72b63d1L, 0x8763e2fbbL, 0x8c9b4f9a2L, 0xa35f5a861L, 0x99e54752cL, 0x2fdb8e16fL, 0x2d083ed68L, 0xa05d36c5eL, 0x5460842feL, 0x173ae0ee6L, 0x38b3c62e5L, 0x476c1ae99L, 0x9a8cb898aL, 0x19d4032acL, 0xa9c01d80bL, 0xca7d5e4deL, 0x295d53115L, 0xb26740e51L, 0xbf21b0988L, 0x167391c15L, 0xd10af35c6L, 0xd94750799L, 0xcb986d117L, 0x01dddf588L, 0x71ed85f46L, 0xa5437d58fL, 0x4029d1e25L, 0xc580ec972L, 0x6847df8baL, 0xe294d997bL, 0xe2e8b10eeL, 0x1593103ddL, 0x222103857L, 0x1e035591dL, 0xb5c9ef2e9L, 0x9f815ec3eL, 0xd1da2a021L, 0x54f171191L, 0xe51f4a05eL, 0xc15e7d603L, 0xba7f16b87L, 0x80b7a83e1L, 0x720e2b18dL, 0x5ec0c069dL, 0xa4f9f689cL, 0x5871cafdaL, 0xc913140a2L, 0x7a8f2efd1L, 0x77064952cL, 0x4ea2d857fL, 0x484523555L, 0x54971a9e3L, 0xeb0694eb2L, 0xb513c8e63L, 0x5c910db58L, 0xca87a4dd7L, 0xb8ca63158L, 0xb4b09431dL, 0x3dc9d50b7L, 0x7d57f02acL, 0x5c595b1b2L, 0x9e0caf698L, 0x136b48555L, 0x687dbcc2bL, 0x54bae2294L, 0x6899bbd7bL, 0x8108f46deL, 0x1dbe8cf08L, 0xa02e1ae1dL, 0x0f5f26d59L, 0x805cf202bL, 0xafede5687L, 0x1583d5b30L, 0xda9ed0620L, 0xcf1237338L, 0x3a5a77bc4L, 0xa17ffa0c6L, 0x29de4c387L, 0x07825d431L, 0x02d7b9b38L, 0x8ed0f26aaL, 0x56e54e30dL, 0x9620ab0e7L, 0xc7e3ea94cL, 0xd288a41e2L, 0xf68884f1eL, 0x5ee02df09L, 0xc02dbf645L, 0xeac4c2424L, 0xcab2d51e1L, 0x037439577L, 0x5618ada43L, 0x2683b5859L, 0x8a607c1ceL, 0x795fd9198L, 0xb3edb11b8L, 0x846939c5cL, 0x8b1f6fa23L, 0xb1a2f2bfeL, 0xb63a07ad7L, 0x5f8ea7b00L, 0x4ee9c6d0cL, 0x990f2889bL, 0xb7f7251d0L, 0xac3291369L, 0x9d8f36a7bL, 0xd57342897L, 0xefca98365L, 0xdacc69f0eL, 0x3a70e4b3cL, 0x1e95c34c2L, 0x4caab6c06L, 0x7231f6ee1L, 0x37909aa04L, 0x048c9a9ccL, 0x59cd081bcL, 0x4dd78c2e4L, 0x4979da10fL, 0x04749d0c5L, 0xa17a4283bL, 0xde7e1d52dL, 0x0e47cedf1L, 0x4fa48cbffL, 0x545a932a0L, 0x6c2bd9eb8L, 0xdd9bd3b8cL, 0x43332c1baL, 0x501fa761dL, 0x7ec40adbbL, 0x4049f2b33L, 0xcde28f57bL, 0xf68c804b9L, 0x8f50fbd3eL, 0x54e1bc344L, 0x36b26e3a2L, 0x02e5ac9b1L, 0x10837858dL, 0x6ccac9e0bL, 0x625ba8a52L, 0xac4c8b45cL, 0x868678237L, 0x4187235feL, 0xbd62663ceL, 0xea832dfb2L, 0xd5a72f0a7L, 0x0659c855eL, 0xbea7f5e48L, 0xff9566715L, 0x1bd06d99aL, 0x9666c578cL, 0xc6527d3ecL, 0xb541f3c61L, 0x678a9ad70L, 0x36eaadfa3L, 0xaf74b01deL, 0x54cc3cdc3L, 0xd2e587ce6L, 0x8694b9349L, 0xd309898feL, 0x5c3250e09L, 0x84dcac28eL, 0xf72add2dfL, 0x1901681a3L, 0x09e6a8fd4L, 0x12f614cd1L, 0x6d7801ac4L, 0x14cf1ca54L, 0x12a7eb608L, 0x5e7a3bf62L, 0x0ba5056a2L, 0x5bee44c9bL, 0x819d7dc86L, 0x062adc8fdL, 0xbd3155d41L, 0xcd8c6b38aL, 0xe320fd50eL, 0xe189d6655L, 0x6863c2831L, 0x0d2b9058fL, 0x23bfad8faL, 0x199bd1216L, 0x56138afd7L, 0xface83a93L, 0x9554da725L, 0x9b614dd91L, 0x98acbca3fL, 0xd5f0d5f21L, 0xeb59039e1L, 0x51d1ec82aL, 0xa366ef3baL, 0x1ad0e01f0L, 0x7f038ad0bL, 0x3ee055321L, 0x3bf2dcbb7L, 0x210e9856cL, 0xe4fea8231L, 0xb89444937L, 0x58852cc34L, 0x1ee29eea9L, 0xb919c79f2L, 0xddc44d3adL, 0xddcbd4777L, 0x3c3982ba1L, 0xdc8ebc45dL, 0x8b97712b1L, 0x9702ea21eL, 0x1f457e726L, 0x27c6f6e26L, 0x0a9797770L, 0xd7615f53bL, 0x74f1cb6e1L, 0xa32e4d7dcL, 0x2e89afd1dL, 0x0b03704d5L, 0xcca58aab0L, 0x1e5749225L, 0x6e63a36baL, 0x562992099L, 0x64701b950L, 0xf94ed6196L, 0xb3441b5f1L, 0xc64fac247L, 0xd72ebd98bL, 0xfa1985b23L, 0x2df788358L, 0x88838b488L, 0x6091032b4L, 0x25ff2d736L, 0xdce63d3d5L, 0xbb5970414L, 0x44d8b5ffeL, 0xe1a5666d8L, 0xe34129125L, 0x0e23854b1L, 0x01b2a6dbeL, 0xd11507bcdL, 0x844531e6bL, 0xd864a8611L, 0xe2a5a7700L, 0x2d178962aL, 0x156b07f01L, 0x48b59fec3L, 0x3d3d9d79cL, 0x1846fb339L, 0xddf1d03caL, 0x0998abaf9L, 0xc9d76190bL, 0x67354a1a8L, 0xcc89e2b09L, 0x353356834L, 0x7ad97470eL, 0xf4d560524L, 0x534b7804eL, 0x14290c632L, 0xb67d39d60L, 0x35b166febL, 0x88e6fb681L, 0xa0f82ae1aL, 0x08460ce52L, 0x8b06a9012L, 0xdaf1299dcL, 0x629ab696cL, 0x3113b448aL, 0x0db5ca215L, 0x3e00b1e2dL, 0x85a87f5abL, 0xb3995ff20L, 0x85661554dL, 0xe709c5384L, 0x0111ca99bL, 0x49e614279L, 0xf14677ec4L, 0x8f6439bfbL, 0x749faa461L, 0x1c4f9189aL, 0xe8e9015caL, 0xf6e68d510L, 0xb3819319fL, 0xda9f7119fL, 0x7787f40f8L, 0xbc57f5716L, 0x60ff2897eL, 0xb3a28a934L, 0x10b34c97cL, 0xc14f53aedL, 0xd3c4eaf5dL, 0xb3148d39eL, 0x07874ea02L, 0xf86692b4aL, 0x5b03a0e8dL, 0xce6db8cc6L, 0x8233d5908L, 0xf163e3c06L, 0xdff854cceL, 0x26706f1bcL, 0x94c358653L, 0x7384c9821L, 0xe51b8e5d5L, 0xeda32963bL, 0xa073f392fL, 0xc3ccfa213L, 0x34adf5216L, 0xcb8da286bL, 0x3b5fbbf08L, 0x12812d1f8L, 0xc97c54c39L, 0xe1c3e36b9L, 0xabb8dc0edL, 0x019dcbbf6L, 0x25b0d7c4dL, 0x045e6b5ceL, 0x17dc086caL, 0xc3f425e6bL, 0x6fdee14f8L, 0x39155e6b4L, 0x0a191ec15L, 0x398fcd7f4L, 0xa6e2b0594L, 0xfe5678d82L, 0xe317eba1fL, 0x2c4f10ca1L, 0xae239c19eL, 0x18e663ed2L, 0x4a040b7e7L, 0xbbca0849cL, 0xce05b3a74L, 0x7cee982fdL, 0x78ee54fa7L, 0x7b47bb0bdL, 0x7e8f19216L, 0xd67d91cedL, 0xef5effe94L, 0xec1d1938dL, 0x4c05ef70eL, 0x0324442d9L, 0xfb0183bb4L, 0xfb7a0bd50L, 0x89aa17d87L, 0xe4e6aed89L, 0xdbecf68b4L, 0x683770de4L, 0xb9f41a136L, 0xc7614caceL, 0x89c298386L, 0x959cf09deL, 0xab30b19e3L, 0xdb2e4b614L, 0x26d30d39bL, 0x6ccefe452L, 0x587c5035cL, 0xea73bbbe0L, 0xdd9d91a11L, 0xdd8c5e851L, 0xe8b4aa077L, 0x8ccf8faddL, 0x47ddd3c0bL, 0x635a92f19L, 0xf0edfd1a3L, 0x1f760bf5eL, 0xa83feb68aL, 0x4f74da9ddL, 0x52f759252L, 0x98bee689eL, 0xc5fc8c3d5L, 0x8373d1286L, 0xf5f1cdabdL, 0xada68d3e5L, 0x3bbb9eb5eL, 0x50cde8478L, 0xf01f956e0L, 0xa922f2842L, 0x233a8b25aL, 0x71118b754L, 0xb7f874552L, 0x44d757121L, 0xb873b14ccL, 0x5bcc1db5cL, 0xbf9b895ceL, 0x5e65bb620L, 0xbbd1ed35cL, 0x358e79973L, 0x62aa5a4a5L, 0x81715fc0fL, 0x8df03a76eL, 0x376b7c6c7L, 0xa07a49f2eL, 0x45e159b63L, 0xdae5706b0L, 0xb5e52c7ccL, 0x206935e8eL, 0x39f0c5119L, 0x3cd58c574L, 0x571986d35L, 0xad66da60fL, 0x02b1a6315L, 0xd0131b533L, 0x741a195c5L, 0x0b8663437L, 0x1cde52798L, 0x6b8e658b1L, 0xb43c0d44dL, 0x45481d697L, 0x29de93df5L, 0x10549b874L, 0xc056b5828L, 0x03fa830adL, 0x9496d14faL, 0xf540592a0L, 0xf31c8b855L, 0x64f2ba36bL, 0xfe7c6e4f5L, 0x5e42a78b0L, 0x9c2b8b096L, 0xdcb4a6e71L, 0xd63b0e7edL, 0xde1bcbcdaL, 0x68e7161f2L, 0x3e5ddf88dL, 0x419a37501L, 0xfad63e7abL, 0xc6e81b4baL, 0x8329315d3L, 0xc88d267e6L, 0x73a0ac25fL, 0xe7b75690fL, 0xdcbb95be2L, 0x7a1d2a059L, 0xd8fac361eL, 0x6312ff5c9L, 0xd2cf50d54L, 0x8c65fd00fL, 0xaa1636532L, 0x870c7285dL, 0x1894f0b84L, 0x4260cc5c3L, 0xe9997b9ecL, 0x87a052144L, 0x8706babf6L, 0xbd5f62ad3L, 0x1a7895439L, 0xf7e294bbcL, 0xbcc27ca26L, 0x3186a63d4L, 0x7f3ede4a4L, 0xb64e32468L, 0x71f250d53L, 0x7c6513783L, 0xb1778714aL, 0x94bf2c57fL, 0x64a9f893aL, 0x1305be654L, 0x493e0c9f6L, 0x05ba6fed8L, 0xc4a0c7a06L, 0x0cc2ec0ddL, 0xd9a6769afL, 0x724c78a49L, 0xc85c981a4L, 0x12553c4cdL, 0x83cb892b1L, 0xbc324ccc7L, 0xef43f6c1dL, 0x2d6748bb7L, 0x5efdce2d7L, 0x94af64f28L, 0xf9d58feb3L, 0xcf547ac63L, 0xceb309febL, 0x30beba8caL, 0x8ab2e486aL, 0x4a95d58adL, 0x25ce07c46L, 0x712b93fd7L, 0x7f46acc81L, 0x64049d4beL, 0x65303aa09L, 0xf3aad21b3L, 0x2903a6cd0L, 0x5a0e0467dL, 0x3c4fa64e4L, 0x5c6655126L, 0xb40a2a67fL, 0xb0c22c6e5L, 0x1507e039bL, 0xb282b16b8L, 0xc0e14a3d3L, 0x93d381427L, 0x6bb55bb87L, 0xb675af72fL, 0xfceb4f95eL, 0x66af6ebbdL, 0x20a44d1f2L, 0x6bc873916L, 0xb8947bee8L, 0x4b6bed8a6L, 0x7012f7867L, 0x7eda3c150L, 0xab3ef1b8eL, 0x6d71466eeL, 0x408c4e225L, 0xe117838b1L, 0x0aef3a075L, 0x5a0779d4fL, 0x70a3b1d69L, 0x26ccd31fdL, 0xed64dd1b2L, 0x981d4f60cL, 0x6a6e4fb61L, 0x52f15fc93L, 0x032b3a64dL, 0xecb17d667L, 0xa983fb935L, 0x37d23c88dL, 0xb8590fbcbL, 0xec2f1a277L, 0x90d3053e6L, 0xa36fa8ccdL, 0x44bd08eccL, 0x61dd197d9L, 0xa307cfd82L, 0x1d09c2de4L, 0x5f6d74368L, 0x1327d1b2dL, 0x594cc36b9L, 0xfea1cba7cL, 0x50c31262dL, 0xd99b1a6baL, 0x1bf789cd2L, 0xe2f6f66f9L, 0x13d5edfc6L, 0xbc3a9ab0cL, 0x1da5b2734L, 0x25ef4f2deL, 0xdcb55a50aL, 0x9c6dbc6acL, 0x89a838853L, 0x168f099eeL, 0xd51601760L, 0x89f324f1aL, 0x2cb1ec1eaL, 0x6306de366L, 0x012a2f11eL, 0xb5c0bf797L, 0x5c5f02be4L, 0x5019f54beL, 0x6ae4a096aL, 0x4bce78778L, 0x94b65b97fL, 0xd3f6e7bd2L, 0x1fbd2a84cL, 0x6d0127ab1L, 0x3e82799aaL, 0x4c1264dfeL, 0xcf69c9360L, 0x4b43e5342L, 0x35d1f0372L, 0xd78c18eb4L, 0x262574101L, 0xc2c5c7335L, 0xbad04051aL, 0x1c481f94eL, 0x3285aa0deL, 0x8973e1f69L, 0x5d238c694L, 0x7b71847b9L, 0x242f5675cL, 0xcc5751c2dL, 0xe09bc620bL, 0x0e4e904ddL, 0x07ca4f1a7L, 0x2ac79ae43L, 0xe213d4250L, 0xd4137c2b5L, 0xddfce11bcL, 0xd1d658566L, 0x213f5b1bbL, 0xcd35be0a8L, 0xcc67d7f91L, 0x509bde098L, 0x74d3d8f46L, 0x51309c970L, 0x53e2bdf66L, 0xa5dd3fed3L, 0xa4e69b212L, 0xb1d39936dL, 0x6b6c8926bL, 0x46540a7b0L, 0x2eebc599fL, 0x2e54a283eL, 0xf9a328a9cL, 0x7ea9cfc53L, 0x5cffa2bdbL, 0x464d16f8eL, 0xeb09444bcL, 0x3f341b259L, 0x4d112b108L, 0x70cb94242L, 0x974ed4ffdL, 0x1084da291L, 0x85673ca39L, 0xd4d74766fL, 0x64a68e1deL, 0xe35630caeL, 0x2073229dbL, 0x63d3a3902L, 0x31598ee06L, 0x808d61126L, 0x029957984L, 0xd4f5f2649L, 0x9ec8a706bL, 0x349981760L, 0xc93ab23a6L, 0x2c7aa80daL, 0x866f102baL, 0xb15cff7bcL, 0x66a13a4caL, 0x54a755048L, 0xd13fdb8d9L, 0x16ad5edf3L, 0xe043bb154L, 0xcc8755671L, 0xcf9b2bfd5L, 0x3608890b4L, 0x330fef315L, 0xe3299ca65L, 0x0b60765e1L, 0x0e9bb17dcL, 0x95f474d8bL, 0xe721d3d00L, 0xd4679e565L, 0xc80da6113L, 0x98deeff30L, 0xc293bb871L, 0xe79132f48L, 0xb152dafbbL, 0x55f6a4386L, 0xa1b8a4044L, 0x4f4187b05L, 0x0b17c2ed3L, 0x95d75ba04L, 0xbbf12e96dL, 0x6abd1a52fL, 0xf300bc991L, 0xf0a7385d4L, 0x52964f82aL, 0xa9962925fL, 0x613b2eef1L, 0x5fd2c92a8L, 0x09ebecd05L, 0x36002b87aL, 0x902c79eefL, 0x394e63c7eL, 0x133285064L, 0xf7cfe2d4bL, 0x4f068522cL, 0x96fea1a0fL, 0xc5a927b13L, 0xe9a2c1994L, 0x5c53b3803L, 0xf636b6188L, 0x007c656e3L, 0x26af1fc5fL, 0xec2f40b78L, 0xfaa1921e5L, 0x6137a8b30L, 0x028674f7bL, 0x3de184e35L, 0xeeef093e6L, 0xd44b3dae0L, 0xbb7ab7d93L, 0x2ae18c956L, 0xcde492bd6L, 0x1cee0216eL, 0xf1e5830adL, 0x76f6c3299L, 0xdea24af84L, 0x277e75586L, 0xa17318024L, 0x5c4739486L, 0x5e3de4725L, 0x6f67c9f6dL, 0x25f42791dL, 0x3c54d15b3L, 0xef98d9c32L, 0x42f64819dL, 0x16d5fd070L, 0x63cb98d4fL, 0x45a3ad27cL, 0x1b496b0acL, 0xaa471c42dL, 0x0599346a2L, 0x0dc8d1c2dL, 0x7498928c1L, 0xea06e90ffL, 0xb683baa32L, 0xf93014e16L, 0x20575d56eL, 0x794325589L, 0x1533e9935L, 0x86b8bcb70L, 0xce11faf5dL, 0x36c0bd318L, 0xe5e8c1167L, 0xe1831ba64L, 0xe088dbfa4L, 0x984479674L, 0xafef02b29L, 0x48518c716L, 0x4301564ceL, 0x21cc88710L, 0xd5c995278L, 0xd8367de1cL, 0x4a51125e8L, 0x113e1c226L, 0xef141e076L, 0x44097011dL, 0x4ca9d707cL, 0x40d8831f1L, 0xbd9c3b1d8L, 0x978364177L, 0x10f7606a9L, 0x46a64270aL, 0x42df1b22bL, 0xe906cf2a0L, 0x997da6fa5L, 0xa5722c26fL, 0xb14f58aaaL, 0xafc167ad8L, 0x37be56e60L, 0xde7f80d62L, 0x0c3fb0a64L, 0xce8ca802cL, 0x35032ed9dL, 0xaa8ba3ee6L, 0x94b2e707cL, 0x2debbdae1L, 0xf53e25fcfL, 0xe935543ebL, 0x1462f0e90L, 0x54ce7d18cL, 0x2ddafdc5fL, 0x700565deeL, 0xfd408e0afL, 0x17d089decL, 0x833ea2459L, 0x3c8d3776aL, 0x2e5eebac8L, 0x20cbf49b0L, 0xc44675eb7L, 0x3a4b6beb1L, 0xce6f37c1eL, 0x63fba2e7cL, 0x5a05b553dL, 0x1286445b0L, 0x5e07a9b61L, 0x7d8397ea4L, 0x8084b7bbbL, 0xb05b38097L, 0x29c3019eeL, 0xed1d2708bL, 0x9df8a4d47L, 0xe4891e436L, 0x2a762ab72L, 0x92f70600fL, 0x92329a2cdL, 0x3e200c6edL, 0x8c0a7233eL, 0x60866806aL, 0xf4fddd24aL, 0xf78464c71L, 0x9c3d22242L, 0x3877ea6d1L, 0xe2a6d54acL, 0x497d2a5e7L, 0xca82f781eL, 0x481524f4cL, 0xdee088814L, 0xb2a82d3a4L, 0x8e6afe6e5L, 0xd6279a5daL, 0x4567cbc1aL, 0x5bec2b2fdL, 0x4ef452505L, 0x61d992cbaL, 0xab96be0cbL, 0x708ef35d9L, 0xb3f6f3623L, 0x36eb1801dL, 0xbadfee917L, 0xa3db13cd0L, 0x1d1a12828L, 0x2500816ceL, 0xcf7612148L, 0x0be6a3f4bL, 0x74142f3daL, 0xce5deed92L, 0xf9530a786L, 0x047c8bb38L, 0xfcabfe88fL, 0xbc83accb1L, 0x20cd9fb1fL, 0x023dcceb3L, 0x9e969b8c4L, 0x6e28de934L, 0x80a399667L, 0x76a0b85adL, 0x21a84be3cL, 0xa28d028b5L, 0xc4e7690dfL, 0xbfd9621e8L, 0x6f4bc0c24L, 0xaa8e76bd7L, 0xdeb55dac9L, 0xbb344fa8bL, 0xfcaab4decL, 0x146aba6cbL, 0xf49ed6eb8L, 0xdd57e9deaL, 0x225d5d090L, 0xd6e86c1c5L, 0x639be5f39L, 0xf5e7a6132L, 0xd2968b09fL, 0x82b30ba1eL, 0x803fa46ccL, 0xc290fab00L, 0x10df59de5L, 0x51ae9dcfbL, 0x49af8516dL, 0x02b564ce6L, 0xc615a1de0L, 0xfef9864a4L, 0xc16e27341L, 0x39e846736L, 0x1ecbb6746L, 0x588d03a7cL, 0x10a0eaf9cL, 0x671ccea6bL, 0x33a154603L, 0xa7b003bc1L, 0xc5fc3848dL, 0x78e50a9c7L, 0x17dbfb88eL, 0x4fd0ed541L, 0x84221debaL, 0x3132cf7e6L, 0xb67e7ac53L, 0xdf6b28024L, 0x785b9f7edL, 0xe3d35320dL, 0x159c06583L, 0x5c54a80a3L, 0xed4d4533bL, 0xcf16c601aL, 0x5e94efbd1L, 0x5d587126eL, 0xeef2f2807L, 0x09f3c558eL, 0x736cfd539L, 0xf5a922ae1L, 0x4e2ab9959L, 0x6a2dd34e7L, 0x8c9d30d23L, 0xeba20b791L, 0xd5c5095e3L, 0x423d75a82L, 0x40cebaafeL, 0x65e08d288L, 0x2e4f6d767L, 0xfe10d2f21L, 0x110347bdaL, 0xe43a9bfb3L, 0xcdea483ccL, 0xfb1e2d8c6L, 0xd8a0af7a7L, 0x37d05b182L, 0x8d1241d83L, 0xda1ea7b6eL, 0x65bea93dbL, 0x2a02f8753L, 0x454243289L, 0x4150bc5a2L, 0xbbabe5911L, 0x4cbcdbc59L, 0xf0e61340bL, 0x30a2cdea8L, 0x5daecb091L, 0x5dc93d891L, 0xc501b4051L, 0x782cfba78L, 0x4c191b61eL, 0xb7e27ef35L, 0x05a476838L, 0x9b0209574L, 0xa775164cfL, 0xd33d21701L, 0x3afcb7d45L, 0x4df2035cdL, 0x498819a21L, 0x293f9e506L, 0x9a35ff1c8L, 0xc090ebe6bL, 0xa4f0551d4L, 0x5dc0dc194L, 0x1388aeb31L, 0x340b27bf4L, 0x3a0f320abL, 0x0996be75dL, 0xb257ecf39L, 0x78d86f2f1L, 0x673f5ff91L, 0x4538d7e3eL, 0xde5bc4369L });
	}
}

