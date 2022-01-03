package com.example.depthmapping.ui.slideshow;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.depthmapping.R;
import com.example.depthmapping.databinding.FragmentSlideshowBinding;

import java.util.Locale;

public class SlideshowFragment extends Fragment implements View.OnClickListener {

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    SharedPreferences prefs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);

        binding.lightTheme.setOnClickListener(this);
        binding.nightTheme.setOnClickListener(this);
        binding.ru.setOnClickListener(this);
        binding.en.setOnClickListener(this);
        binding.de.setOnClickListener(this);

        prefs= getActivity().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);

        if(prefs.getString("theme", "light").equals("light")){
            binding.lightTheme.setChecked(true);
        }else if (prefs.getString("theme", "light").equals("night")){
            binding.nightTheme.setChecked(true);
        }

        switch (prefs.getString("lang", "ru")){
            case "ru":
                binding.ru.setChecked(true);
                break;
            case "en":
                binding.en.setChecked(true);
                break;
            case "de":
                binding.de.setChecked(true);
                break;
        }

        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onClick(View v) {

        getActivity().overridePendingTransition(R.anim.alpha, R.anim.alpha);

        switch (v.getId()){
            case R.id.nightTheme:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                prefs.edit().putString("theme", "night").commit();
                getActivity().recreate();
                break;
            case R.id.lightTheme:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                prefs.edit().putString("theme", "light").commit();
                getActivity().recreate();
                break;

            case R.id.ru:
                Locale localeRu = new Locale("ru");
                Locale.setDefault(localeRu);
                Configuration configurationRu = new Configuration();
                configurationRu.locale = localeRu;
                getActivity().getResources().updateConfiguration(configurationRu, null);
                prefs.edit().putString("lang", "ru").commit();
                getActivity().recreate();
                break;
            case R.id.en:
                Locale localeEn = new Locale("en");
                Locale.setDefault(localeEn);
                Configuration configurationEn = new Configuration();
                configurationEn.locale = localeEn;
                getActivity().getResources().updateConfiguration(configurationEn, null);
                prefs.edit().putString("lang", "en").commit();
                getActivity().recreate();
                break;
            case R.id.de:
                Locale localeDe = new Locale("de");
                Locale.setDefault(localeDe);
                Configuration configurationDe = new Configuration();
                configurationDe.locale = localeDe;
                getActivity().getResources().updateConfiguration(configurationDe, null);
                prefs.edit().putString("lang", "de").commit();
                getActivity().recreate();
                break;
        }
    }

    public NavController getNavController(){
        return Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);

    }
}