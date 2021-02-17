package com.example.portfolioapp.Enum;

import android.graphics.Color;

import com.example.portfolioapp.R;

public enum PasswordStrength {

    Weak(R.string.weak, Color.parseColor("#FF4646")),
    Medium(R.string.medium, Color.parseColor("#ff8f00")),
    Strong(R.string.strong, Color.parseColor("#FCCA3F")),
    Very_Strong(R.string.very_strong, Color.parseColor("#00c853"));

    public int msg;
    public int color;


    public boolean length;
    private static int MIN_LENGTH = 8;
    private static int MAX_Length = 15;


    PasswordStrength(int msg, int color)
    {
        this.msg=msg;
        this.color=color;
    }

    public static PasswordStrength calculate(String password)
    {
        int score = 0;
        boolean upper = false;
        boolean lower = false;
        boolean digit = false;
        boolean special = false;

        for(int i=0;i<password.length();i++)
        {
            char c = password.charAt(i);

            if(!special && !Character.isLetterOrDigit(c))
            {
                score++;
                special=true;
            }
            else
            {
                if(!digit && Character.isDigit(c))
                {
                    score++;
                    digit=true;
                }
                else
                {
                    if(!upper || !lower){
                        if(Character.isUpperCase(c))
                        {
                            upper=true;
                        }
                        else
                        {
                            lower=true;
                        }

                        if(upper && lower)
                        {
                            score++;
                        }
                    }
                }
            }
        }

        if(password.length()>=MAX_Length)
        {
            score++;
        }
        else if(password.length()<MIN_LENGTH)
        {
            score=0;
        }

        switch(score){
            case 0: return Weak;
            case 1: return Medium;
            case 2: return Strong;
            case 3: return Very_Strong;
            default:
        }

        return Very_Strong;

    }




}
