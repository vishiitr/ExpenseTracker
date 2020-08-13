package com.iitr.vishal.expensetracker.Processor.Banks;

import com.iitr.vishal.expensetracker.Model.SmsModel;
import com.iitr.vishal.expensetracker.Model.TranscationModel;
import com.iitr.vishal.expensetracker.Processor.BankProcessor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenericBankProcessor implements BankProcessor.IBankProcessor {
    private static Pattern cardNumber = Pattern.compile("(?i)(?:\\smade on|your|made a|spent|debited\\s|in\\*).+?card.+?(\\d{4,5})");
    private static Pattern cardNumberReminder = Pattern.compile("(?i).+?card.+?(\\d{4,5}).+");
    private static Pattern merchant = Pattern.compile("(?i)(?:\\sat\\s|Info:|in\\*)([A-Za-z0-9-]*\\s?-?\\s?[A-Za-z0-9]*\\s?-?\\.?)");
    private static Pattern amount = Pattern.compile("(?i)(?:(?:RS|INR|MRP)\\.?\\s?)(\\d+(:?\\,\\d+)?(\\,\\d+)?(\\.\\d{1,2})?)");
    private static Pattern date = Pattern.compile("(\\d{1,4}([.\\-\\/])\\w{1,3}([.\\-\\/])\\d{1,4})");
    private String BankName;

    public GenericBankProcessor(String bankName)
    {
        BankName = bankName;
    }

    @Override
    public TranscationModel onSaveTranscation(SmsModel smsModel) {
        TranscationModel transcationModel = new TranscationModel();
        transcationModel.bankName = BankName;
        transcationModel.smsId = Integer.parseInt(smsModel.getId());
        int matching = 0;

        //Merchant
        Matcher m = merchant.matcher(smsModel.getMsg());
        if(m.find()) {
            transcationModel.spentAt = m.group(1);
            matching++;
        }

        if(transcationModel.spentAt!=null) // Go for Transcation SMS
        {
            //Card Number
            m = cardNumber.matcher(smsModel.getMsg());
            if(m.find()) {
                transcationModel.spendingCard = m.group(1);
                matching++;
            }
        }
        else if(containsIgnoreCase(smsModel.getMsg(),"due") && containsIgnoreCase(smsModel.getMsg(),"total") )
        {
            m = cardNumberReminder.matcher(smsModel.getMsg());
            if(m.find()) {
                transcationModel.spendingCard = m.group(1);
                matching++;
            }
        }

        //Amount
        m = amount.matcher(smsModel.getMsg());
        boolean spentAmountSet = false;
        while(m.find()) {
            String amount = m.group(1);
            if(!spentAmountSet)
            {
                transcationModel.spentAmount = Float.parseFloat(amount.replaceAll(",", ""));
                matching++;
                spentAmountSet = true;
            }
            else
            {
                transcationModel.availableBalance = Float.parseFloat(amount.replaceAll(",", ""));
            }
        }

        //Date
        m = date.matcher(smsModel.getMsg());
        if(m.find()) {
            String date = m.group(1);
            Date convertedDate = convertToDate(date);
            if(convertedDate == null)
                convertedDate = convertToDate2(date);
            if(convertedDate == null)
                convertedDate = convertToDate3(date);
            transcationModel.spentDate = convertedDate;
            matching++;
        }
        if(matching==4 || (transcationModel.spentAt == null && matching == 3))
            return transcationModel;
        else
            return null;
    }

    private Date convertToDate(String spentDate) {
        DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yy");
        Date date = null;
        try {
            date = sourceFormat.parse(spentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private Date convertToDate2(String spentDate) {
        DateFormat sourceFormat = new SimpleDateFormat("dd-MMM-yy");
        Date date = null;
        try {
            date = sourceFormat.parse(spentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    private Date convertToDate3(String spentDate) {
        DateFormat sourceFormat = new SimpleDateFormat("dd-MM-yy");
        Date date = null;
        try {
            date = sourceFormat.parse(spentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }
}
