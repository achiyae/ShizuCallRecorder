/*
 * ShizuCallRecorder: FOSS Call recording powered through ADB/Shizuku!
 *  Copyright (C) 2026-present kitsumed (Med)
 *  This software is licensed under the GNU General Public License v3 or later, with additional terms as permitted under Section 7.
 *  The full license text is available in the LICENSE file at the root of this project.
 *  This software is distributed WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package com.kitsumed.shizucallrecorder.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class PhoneNumberManagerTest {

    @Test
    fun contactLookup_keepsStarInShortCode() {
        assertEquals("*2700", PhoneNumberManager.normaliseForContactLookup("*2700"))
    }

    @Test
    fun contactLookup_keepsHashInServiceCode() {
        assertEquals("*100#", PhoneNumberManager.normaliseForContactLookup("*100#"))
    }

    @Test
    fun contactLookup_stripsFormattingFromRegularNumber() {
        assertEquals("+972544280051", PhoneNumberManager.normaliseForContactLookup("+972 54-428-0051"))
    }

    @Test
    fun contactLookup_returnsEmptyForAnonymousToken() {
        assertEquals("", PhoneNumberManager.normaliseForContactLookup("Private"))
    }

    @Test
    fun contactLookup_returnsEmptyForBlank() {
        assertEquals("", PhoneNumberManager.normaliseForContactLookup("  "))
    }

    @Test
    fun normalise_stillStripsStarForOtherUses() {
        assertEquals("2700", PhoneNumberManager.normalisePhoneNumber("*2700"))
    }
}
