package com.phonebook.dipanjal.webviewtest;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * Created by Dipanjal on 4/13/2017.
 */

public class WebAppClass {

    MainActivity webActivity = null;
    WebView webView=null;
    String curOperator=null;
    String[] digit=new String[0];
    boolean arithExp=false , isSummedBefore=false;
    float lastDigit;
    float finalSum;
    String pendedExp=null;


    WebAppClass(Context context ,WebView webviewObj)
    {
        this.webActivity= (MainActivity) context;
        this.webView=webviewObj;
    }

    @JavascriptInterface
    public void showToast(String str)
    {
        Toast.makeText(webActivity,str,Toast.LENGTH_LONG).show();
    }
    @JavascriptInterface
    public void addNum()
    {
        this.arithExp=false;
        this.isSummedBefore=false;
        //Toast.makeText(webActivity,"Excp : "+String.valueOf(this.arithExp),Toast.LENGTH_LONG).show();
    }
    @JavascriptInterface
    public void addOperator(String op)
    {
        this.curOperator=op;
        this.isSummedBefore=false;
        this.arithExp=false;
        //tToast.makeText(webActivity,"Excp : "+String.valueOf(this.arithExp),Toast.LENGTH_LONG).show();
        //Toast.makeText(webActivity,"OPR : "+this.curOperator,Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public String getResult(String exp)
    {
        if(isSummedBefore)
        {
            this.doAutoSum();
            String res=String.valueOf(this.finalSum);
            return res;
        }
        else
        {
            String numPat="^(-)?[0-9]+([.][0-9]+)?[+*/-][0-9]+([.][0-9]+)?$";
            if(Pattern.matches(numPat,exp)) //the exp is valid i mean mathces with the pattarn
            {

                //Toast.makeText(webActivity,"exp: "+exp+" is Valid",Toast.LENGTH_LONG).show();
                this.doSum(exp);
                if(this.arithExp)
                {
                    String res=this.digit[0]+this.curOperator;
                    Toast.makeText(webActivity,"Can't Devided by 0",Toast.LENGTH_LONG).show();
                    return res;
                }
                else
                {
                    String res=String.valueOf(this.finalSum);
                    return res;
                }
            }
            else
            {

                Toast.makeText(webActivity,"Invalid Expression",Toast.LENGTH_LONG).show();
                return exp; //retrurning the invalid sting hence js display will as it is
            }
        }



    }

    private void doSum(String exp)
    {
        switch (this.curOperator)
        {
            case "+":
            {
                this.digit=exp.split(Pattern.quote("+"));
                //this.backGroundActivity(digit);
                this.finalSum=Float.parseFloat(digit[0])+Float.parseFloat(digit[1]);
                this.lastDigit=Float.parseFloat(digit[1]);
                this.isSummedBefore=true; //setting a flag that a sumation is done so we can do autosum furture
                break;
            }
            case "-":
            {
                this.digit=exp.split("-");
                //this.backGroundActivity(digit);
                this.finalSum=Float.parseFloat(digit[0])-Float.parseFloat(digit[1]);
                this.lastDigit=Float.parseFloat(digit[1]);
                this.isSummedBefore=true;
                //Toast.makeText(getApplicationContext()," - ",Toast.LENGTH_LONG).show();
                break;
            }
            case "*":
            {
                this.digit=exp.split(Pattern.quote("*"));
                //this.backGroundActivity(digit);
                this.finalSum=Float.parseFloat(digit[0])*Float.parseFloat(digit[1]);
                this.lastDigit=Float.parseFloat(digit[1]);
                this.isSummedBefore=true;
                //Toast.makeText(getApplicationContext()," * ",Toast.LENGTH_LONG).show();
                break;
            }
            case "/":
            {
                this.digit=exp.split("/");
                if(Float.parseFloat(digit[1])!=0)
                {
                    //this.backGroundActivity(digit);
                    this.finalSum=Float.parseFloat(digit[0])/Float.parseFloat(digit[1]);
                    this.lastDigit=Float.parseFloat(digit[1]);
                    this.isSummedBefore=true;
                }
                else
                {
                    this.arithExp=true;
                    Toast.makeText(this.webActivity,"Excp: "+String.valueOf(arithExp),Toast.LENGTH_LONG).show();
                }

                /*try{
                        this.finalSum=Float.parseFloat(digit[0])/Float.parseFloat(digit[1]);
                        this.lastDigit=Float.parseFloat(digit[1]);
                    }
                catch (ArithmeticException e)
                    {
                        this.arithExp=true;
                        this.display.setText(digit[0]+"/");
                        this.subDisplay.setText(digit[0]+"/");
                        isExpection=true;
                        this.opDigit=false;
                        Toast.makeText(getApplicationContext(),"Can't Devided by Zero",Toast.LENGTH_LONG).show();
                    }*/
                //Toast.makeText(getApplicationContext()," / ",Toast.LENGTH_LONG).show();
                break;
            }
            default:{
                break;
            }

        }
    }
    private void doAutoSum()
    {
        switch (this.curOperator)
        {
            case "+":
            {
                this.finalSum+=this.lastDigit;
                break;
            }
            case "-":
            {
                this.finalSum-=this.lastDigit;
                break;
            }
            case "*":
            {
                this.finalSum*=this.lastDigit;
                break;
            }
            case "/":
            {
                try {
                    this.finalSum /= this.lastDigit;
                }catch (ArithmeticException e)
                {
                    Toast.makeText(webActivity,"Can't Devided by Zero",Toast.LENGTH_LONG).show();
                }
                break;
            }
            default:
            {
                break;
            }
        }
    }

    @JavascriptInterface
    public boolean reloadActivity()
    {
        //Toast.makeText(webActivity,String.valueOf(webActivity.isNetworkAvailable()),Toast.LENGTH_LONG).show();
        if(webActivity.isNetworkAvailable())
        {
            Toast.makeText(webActivity,"Enable",Toast.LENGTH_LONG).show();
            return true;
        }
        else
        {
            Toast.makeText(webActivity,"Disable",Toast.LENGTH_LONG).show();
            return  false;
        }

    }
}
