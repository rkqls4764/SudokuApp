package com.gabeen.sudokuapp2.ui.ad

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class RewardedAdManager(
    private val context: Context
) {
    private var rewardedAd: RewardedAd? = null

    // 광고 미리 로드
    fun loadAd(adUnitId: String) {
        RewardedAd.load(
            context,
            adUnitId,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {

                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                }
            }
        )
    }

    // 광고 보여주고 콜백 실행
    fun showAd(
        activity: Activity,
        onReward: () -> Unit
    ) {
        rewardedAd?.show(activity) {
            onReward()
        }
    }
}