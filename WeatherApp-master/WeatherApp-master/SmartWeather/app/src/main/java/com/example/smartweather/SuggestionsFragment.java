package com.example.smartweather;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SuggestionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuggestionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SuggestionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SuggestionsFragment newInstance(String param1, String param2) {
        SuggestionsFragment fragment = new SuggestionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_suggestions, container, false);
        updateFood(view);
        updateActivities(view);
        updateClothing(view);

        return view;
    }

    public void updateFood(View view) {
        int temp = 0;
        int rain = 1;
        int[] foodItems;
        foodItems = getFood(temp, rain);

        ImageView img = view.findViewById(R.id.food_suggestion_1);
        img.setImageResource(foodItems[0]);

        img = view.findViewById(R.id.food_suggestion_2);
        img.setImageResource(foodItems[1]);

        img = view.findViewById(R.id.food_suggestion_3);
        img.setImageResource(foodItems[2]);

        img = view.findViewById(R.id.food_suggestion_4);
        img.setImageResource(foodItems[3]);

    }
    //Restructuring the Code

    public void updateActivities(View view) {
        int temp = 2;
        int rain = 1;
        int[] activitiesItems = getActivities(temp, rain);
        ImageView img = (view.findViewById(R.id.activity_suggestion_1));
        img.setImageResource(activitiesItems[0]);

        img = (view.findViewById(R.id.activity_suggestion_2));
        img.setImageResource(activitiesItems[1]);

        img = (view.findViewById(R.id.activity_suggestion_3));
        img.setImageResource(activitiesItems[2]);

        img = (view.findViewById(R.id.activity_suggestion_4));
        img.setImageResource(activitiesItems[3]);


    }

    public void updateClothing(View view) {
        int temp = 0;
        int rain = 0;
        int[] clothingItems = getClothing(temp, rain);
        ImageView img = (view.findViewById(R.id.clothing_suggestion_1));
        img.setImageResource(clothingItems[0]);

        img = (view.findViewById(R.id.clothing_suggestion_2));
        img.setImageResource(clothingItems[1]);
    }

    public int[] getFood(int temp, int rain) {
        int[] arr = new int[4];


        switch (temp * 10 + rain) {

           //Case 0 :  temp 0 rain 0: normal temp , no rain :
            //food : corn, friedrice, ice-cream, fruitjuices
            case 0:
               arr[0] = R.drawable.sugg_food_corn;
               arr[1] = R.drawable.friedrice;
               arr[2] = R.drawable.sugg_food_icecream;
               arr[3] = R.drawable.sugg_food_fruitjuices;
                break;

            //1 :  temp 0 rain 1: normal temp , rain:
            //food : corn, friedrice, tea, fruitjuices

            case 1:
                arr[0] = R.drawable.sugg_food_corn;
                arr[1] = R.drawable.sugg_food_friedrice;
                arr[2] = R.drawable.sugg_food_tea;
                arr[3] = R.drawable.sugg_food_fruitjuices;
                break;

            //10 :  temp 1 rain 0: low temp , no rain :
            //food : meat_eggs, friedrice, soup, tea

            case 10:
                arr[0] = R.drawable.sugg_meategg;
                arr[1] = R.drawable.sugg_food_friedrice;
                arr[2] = R.drawable.sugg_food_soup;
                arr[3] = R.drawable.sugg_food_tea;
                break;

            // 11 :  temp 1 rain 1: Low temp , rain:
            //food : barbecue, friedrice, soup, tea

            case 11:
                arr[0] = R.drawable.sugg_food_barbecue;
                arr[1] = R.drawable.sugg_food_friedrice;
                arr[2] = R.drawable.sugg_food_soup;
                arr[3] = R.drawable.sugg_food_tea;
                break;

            //20 :  temp 2 rain 0: high temp  , no rain:
            //food : corn, fruitjuices, fruits, ice-cream

            case 20:
               // arr[0] = R.drawable.sugg_food_corn;
                arr[1] = R.drawable.sugg_food_fruitjuices;
                arr[2] = R.drawable.sugg_food_fruits;
                arr[3] = R.drawable.sugg_food_icecream;
                break;

            //21 :  temp 2 rain 0: high temp  , no rain:
            //food : tea, fruitjuices, fruits, ice-cream

            case 21:
                arr[0] = R.drawable.sugg_food_tea;
                arr[1] = R.drawable.sugg_food_fruitjuices;
                arr[2] = R.drawable.sugg_food_fruits;
                arr[3] = R.drawable.sugg_food_icecream;
                break;


            default:

                break;


        }


            return arr;
        }
        public int[] getActivities ( int temp, int rain){
            int[] arr = new int[4];


        switch (temp * 10 + rain) {
            //Case 0 :  temp 0 rain 0: normal temp , no rain :
            //activities : football, running, swimming, tennis

            case 0:
                arr[0] = R.drawable.sugg_activity_football;
                arr[1] = R.drawable.sugg_activity_running;
                arr[2] = R.drawable.sugg_activity_swimming;
                arr[3] = R.drawable.sugg_activity_tennis;
                break;

            //Case 1 :  temp 0 rain 1: normal temp , rain :
            //activities : gym, yoga, library, longdrive
            case 1:
                arr[0] = R.drawable.sugg_activity_gym;
                arr[1] = R.drawable.sugg_activity_yoga;
                arr[2] = R.drawable.sugg_activity_library;
                arr[3] = R.drawable.sugg_activity_longdrive;
                break;
            //Case 10 : temp 1 rain 0: low temp , no rain :
            //activities : icehockey, running, ski, terrain

            case 10:
                arr[0] = R.drawable.sugg_activity_icehockey;
                arr[1] = R.drawable.sugg_activity_running;
                arr[2] = R.drawable.sugg_activity_ski;
                arr[3] = R.drawable.sugg_activity_terrain;
                break;
            //Case 11 : temp 1 rain 0: low temp , rain :
            //activities : barbecue, library, longdrive, movies
            case 11:
                arr[0] = R.drawable.sugg_activity_barbecue;
                arr[1] = R.drawable.sugg_activity_library;
                arr[2] = R.drawable.sugg_activity_longdrive;
                arr[3] = R.drawable.sugg_activity_movies;
                break;
            //Case 20 :  temp 2 rain 0: high temp, no rain:
            //activities : swimming, library, jetski , scuba

            case 20:
                arr[0] = R.drawable.sugg_activity_swimming;
                arr[1] = R.drawable.sugg_activity_library;
                arr[2] = R.drawable.sugg_activity_jetski;
                arr[3] = R.drawable.sugg_activity_scuba;
                break;
            //Case 21 :  temp 2 rain 0: high temp, rain:
            //activities : aerobics, library, movies, longdrive

            case 21:
                arr[0] = R.drawable.sugg_activity_aerobics;
                arr[1] = R.drawable.sugg_activity_library;
                arr[2] = R.drawable.sugg_activity_movies;
                arr[3] = R.drawable.sugg_activity_longdrive;
                break;
            default:

                break;
        }

            return arr;
        }

        public int[] getClothing ( int temp, int rain){
            int[] arr = new int[2];


        switch (temp * 10 + rain) {
            //Case 0 :  temp 0 rain 0: normal temp , no rain :
            //clothing: normal1, normal2

            case 0:

                arr[0] = R.drawable.sugg_clothing_normal1;
                arr[1] = R.drawable.sugg_clothing_normal2;

                break;
            //Case 1 :  temp 0 rain 1: normal temp , rain :
            //clothing: normal1, rainy1
            case 1:
                arr[0] = R.drawable.sugg_clothing_normal1;
                arr[1] = R.drawable.sugg_clothing_rainy1;


                break;

            //Case 10 :  temp 1 rain 0: low temp , no rain :
            //clothing: winter1, winter2
            case 10:
                arr[0] = R.drawable.sugg_clothing_winter1;
                arr[1] = R.drawable.sugg_clothing_winter2;


                break;

            //Case 11 :  temp 1 rain 1: low temp , rain :
            //clothing: winter1, rainy1

            case 11:
                arr[0] = R.drawable.sugg_clothing_winter1;
                arr[1] = R.drawable.sugg_clothing_rainy1;


                break;

            //Case 20 :  temp 2 rain 0: high temp , no rain :
            //clothing: summer1, summer2
            case 20:
                arr[0] = R.drawable.sugg_clothing_summer1;
                arr[1] = R.drawable.sugg_clothing_summer2;

                break;


            //Case 21 :  temp 2 rain 1: high temp , rain :
            //clothing: summer1, rainy1
            case 21:
                arr[0] = R.drawable.sugg_clothing_summer1;
                arr[1] = R.drawable.sugg_clothing_rainy1;
                break;

            default:

                break;

        }


            return arr;
        }
    }
