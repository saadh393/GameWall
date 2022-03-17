package com.wwinc.moviewall.share

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.wwinc.moviewall.BuildConfig
import com.wwinc.moviewall.MainActivity
import com.wwinc.moviewall.R
import com.wwinc.moviewall.databinding.FragmentShareBinding

class Share : Fragment() {

    lateinit var binding : FragmentShareBinding

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?    ): View? {
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_share, container, false)
       setHasOptionsMenu(false);
        binding.shareBtn.setOnClickListener {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
                var shareMessage = "Let me recommend you this application\n\nhttps://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "Choose One ❤"))
            } catch (e: Exception) {
                //e.toString();
            }
        }

        return binding.root
    }


}