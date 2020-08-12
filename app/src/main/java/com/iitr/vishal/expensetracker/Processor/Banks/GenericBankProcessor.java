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
        //Card Number
        Matcher m = cardNumber.matcher(smsModel.getMsg());
        if(m.find()) {
            transcationModel.spendingCard = m.group(1);
            matching++;
        }

        //Merchant
        m = merchant.matcher(smsModel.getMsg());
        if(m.find()) {
            transcationModel.spentAt = m.group(1);
            matching++;
        }

        //Amount
        m = amount.matcher(smsModel.getMsg());
        if(m.find()) {
            String amount = m.group(1);
            transcationModel.spentAmount = Float.parseFloat(amount.replaceAll(",", ""));
            /*if(m.groupCount()>2)
            {
                amount = m.group(2);
                transcationModel.availableBalance = Float.parseFloat(amount.replaceAll(",", ""));
            }*/
            matching++;
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
        if(matching==4)
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
}
