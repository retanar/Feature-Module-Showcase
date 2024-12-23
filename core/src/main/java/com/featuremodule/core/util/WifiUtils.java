package com.featuremodule.core.util;

/**
 * Copy of ScanResult utility functions unavailable on earlier SDKs
 */
public class WifiUtils {
    /**
     * The unspecified value.
     */
    public final static int UNSPECIFIED = -1;

    /**
     * 2.4 GHz band first channel number
     */
    public static final int BAND_24_GHZ_FIRST_CH_NUM = 1;
    /**
     * 2.4 GHz band last channel number
     */
    public static final int BAND_24_GHZ_LAST_CH_NUM = 14;
    /**
     * 2.4 GHz band frequency of first channel in MHz
     */
    public static final int BAND_24_GHZ_START_FREQ_MHZ = 2412;
    /**
     * 2.4 GHz band frequency of last channel in MHz
     */
    public static final int BAND_24_GHZ_END_FREQ_MHZ = 2484;

    /**
     * 5 GHz band first channel number
     */
    public static final int BAND_5_GHZ_FIRST_CH_NUM = 32;
    /**
     * 5 GHz band last channel number
     */
    public static final int BAND_5_GHZ_LAST_CH_NUM = 177;
    /**
     * 5 GHz band frequency of first channel in MHz
     */
    public static final int BAND_5_GHZ_START_FREQ_MHZ = 5160;
    /**
     * 5 GHz band frequency of last channel in MHz
     */
    public static final int BAND_5_GHZ_END_FREQ_MHZ = 5885;

    /**
     * 6 GHz band first channel number
     */
    public static final int BAND_6_GHZ_FIRST_CH_NUM = 1;
    /**
     * 6 GHz band last channel number
     */
    public static final int BAND_6_GHZ_LAST_CH_NUM = 233;
    /**
     * 6 GHz band frequency of first channel in MHz
     */
    public static final int BAND_6_GHZ_START_FREQ_MHZ = 5955;
    /**
     * 6 GHz band frequency of last channel in MHz
     */
    public static final int BAND_6_GHZ_END_FREQ_MHZ = 7115;
    /**
     * The center frequency of the first 6Ghz preferred scanning channel, as defined by
     * IEEE802.11ax draft 7.0 section 26.17.2.3.3.
     */
    public static final int BAND_6_GHZ_PSC_START_MHZ = 5975;
    /**
     * The number of MHz to increment in order to get the next 6Ghz preferred scanning channel
     * as defined by IEEE802.11ax draft 7.0 section 26.17.2.3.3.
     */
    public static final int BAND_6_GHZ_PSC_STEP_SIZE_MHZ = 80;

    /**
     * 6 GHz band operating class 136 channel 2 center frequency in MHz
     */
    public static final int BAND_6_GHZ_OP_CLASS_136_CH_2_FREQ_MHZ = 5935;

    /**
     * 60 GHz band first channel number
     */
    public static final int BAND_60_GHZ_FIRST_CH_NUM = 1;
    /**
     * 60 GHz band last channel number
     */
    public static final int BAND_60_GHZ_LAST_CH_NUM = 6;
    /**
     * 60 GHz band frequency of first channel in MHz
     */
    public static final int BAND_60_GHZ_START_FREQ_MHZ = 58320;
    /**
     * 60 GHz band frequency of last channel in MHz
     */
    public static final int BAND_60_GHZ_END_FREQ_MHZ = 70200;

    /**
     * Utility function to check if a frequency within 2.4 GHz band
     *
     * @param freqMhz frequency in MHz
     * @return true if within 2.4GHz, false otherwise
     */
    public static boolean is24GHz(int freqMhz) {
        return freqMhz >= BAND_24_GHZ_START_FREQ_MHZ && freqMhz <= BAND_24_GHZ_END_FREQ_MHZ;
    }

    /**
     * Utility function to check if a frequency within 5 GHz band
     *
     * @param freqMhz frequency in MHz
     * @return true if within 5GHz, false otherwise
     */
    public static boolean is5GHz(int freqMhz) {
        return freqMhz >= BAND_5_GHZ_START_FREQ_MHZ && freqMhz <= BAND_5_GHZ_END_FREQ_MHZ;
    }

    /**
     * Utility function to check if a frequency within 6 GHz band
     *
     * @return true if within 6GHz, false otherwise
     */
    public static boolean is6GHz(int freqMhz) {
        if (freqMhz == BAND_6_GHZ_OP_CLASS_136_CH_2_FREQ_MHZ) {
            return true;
        }
        return (freqMhz >= BAND_6_GHZ_START_FREQ_MHZ && freqMhz <= BAND_6_GHZ_END_FREQ_MHZ);
    }

    /**
     * Utility function to check if a frequency is 6Ghz PSC channel.
     *
     * @return true if the frequency is 6GHz PSC, false otherwise
     */
    public static boolean is6GHzPsc(int freqMhz) {
        if (!is6GHz(freqMhz)) {
            return false;
        }
        return (freqMhz - BAND_6_GHZ_PSC_START_MHZ) % BAND_6_GHZ_PSC_STEP_SIZE_MHZ == 0;
    }

    /**
     * Utility function to check if a frequency within 60 GHz band
     *
     * @return true if within 60GHz, false otherwise
     */
    public static boolean is60GHz(int freqMhz) {
        return freqMhz >= BAND_60_GHZ_START_FREQ_MHZ && freqMhz <= BAND_60_GHZ_END_FREQ_MHZ;
    }

    /**
     * Utility function to convert frequency in MHz to channel number.
     *
     * @param freqMhz frequency in MHz
     * @return channel number associated with given frequency, {@link #UNSPECIFIED} if no match
     */
    public static int convertFrequencyMhzToChannelIfSupported(int freqMhz) {
        // Special case
        if (freqMhz == 2484) {
            return 14;
        } else if (is24GHz(freqMhz)) {
            return (freqMhz - BAND_24_GHZ_START_FREQ_MHZ) / 5 + BAND_24_GHZ_FIRST_CH_NUM;
        } else if (is5GHz(freqMhz)) {
            return ((freqMhz - BAND_5_GHZ_START_FREQ_MHZ) / 5) + BAND_5_GHZ_FIRST_CH_NUM;
        } else if (is6GHz(freqMhz)) {
            if (freqMhz == BAND_6_GHZ_OP_CLASS_136_CH_2_FREQ_MHZ) {
                return 2;
            }
            return ((freqMhz - BAND_6_GHZ_START_FREQ_MHZ) / 5) + BAND_6_GHZ_FIRST_CH_NUM;
        } else if (is60GHz(freqMhz)) {
            return ((freqMhz - BAND_60_GHZ_START_FREQ_MHZ) / 2160) + BAND_60_GHZ_FIRST_CH_NUM;
        }

        return UNSPECIFIED;
    }
}
