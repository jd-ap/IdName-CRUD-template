CREATE ALIAS fuzzy AS '
    boolean searchString(String text, String pattern, int k) {
        int result = -1;
        int m = pattern.length();

        if (null == pattern || pattern.length() == 0) return Boolean.FALSE;
        if (m > 31) return Boolean.FALSE; //Error: The pattern is too long!

        int i, d;
        int[] R = new int[(k + 1) * 4];
        int[] patternMask = new int[255];

        for (i = 0; i <= k; ++i)
            R[i] = ~1; //(Integer.MAX_VALUE)

        for (i = 0; i <= 127; ++i)
            patternMask[i] = ~0;

        for (i = 0; i < m; ++i)
            patternMask[pattern.charAt(i)] &= ~(1 << i);

        for (i = 0; i < text.length(); ++i) {
            int oldRd1 = R[0];
            char cAt = text.charAt(i);

            R[0] |= patternMask[cAt];
            R[0] <<= 1;

            for (d = 1; d <= k; ++d) {
                int tmp = R[d];

                R[d] = (oldRd1 & (R[d] | patternMask[cAt])) << 1;
                oldRd1 = tmp;
            }

            if (0 == (R[k] & (1 << m))) {
                result = (i - m) + 1;
                break;
            }
        }

        return 0 <= result;
    }
';