package com.iitr.vishal.expensetracker.Processor.Banks;

import com.iitr.vishal.expensetracker.Common.Constants;
import com.iitr.vishal.expensetracker.Common.Formatter;
import com.iitr.vishal.expensetracker.Model.SmsModel;
import com.iitr.vishal.expensetracker.Model.TranscationModel;
import com.iitr.vishal.expensetracker.Processor.BankProcessor;

import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Divya on 17-03-2018.
 */

public class AmexBankProcessor implements BankProcessor.IBankProcessor {
    private final static String spendingRegex = "A charge.*" + Constants.RegexConstants.Money + ".* initiated .*" + Constants.RegexConstants.CardAmex +  " at " + Constants.RegexConstants.Merchant  + " on " + Constants.RegexConstants.DateWithNumber + ".*";
    private final static String spendingRegex2 = "Alert: You've spent *" + Constants.RegexConstants.Money + ".* card .*" + Constants.RegexConstants.CardAmex +  " at " + Constants.RegexConstants.Merchant  + " on " + Constants.RegexConstants.DateWithNumber + ".*";
    private final static String reminderRegex = "Reminder: .*\\*\\*\\*(\\d{4}).*" + Constants.RegexConstants.DateWithName + ".*=" + Constants.RegexConstants.Money + ".*";
    private final static String billingRegex = "Dear Customer, your statement .* \\*{10}(\\d{5}) .*total payment " + Constants.RegexConstants.Money +" is due by " + Constants.RegexConstants.DateWithNumber +".*";
    @Override
    public TranscationModel onSaveTranscation(SmsModel smsModel) {
        Pattern spendPattern = Pattern.compile(spendingRegex);
        Matcher spendMatcher = spendPattern.matcher(smsModel.getMsg());

        Pattern spendPattern2 = Pattern.compile(spendingRegex2);
        Matcher spendMatcher2 = spendPattern2.matcher(smsModel.getMsg());

        Pattern reminderPattern = Pattern.compile(reminderRegex);
        Matcher reminderMatcher = reminderPattern.matcher(smsModel.getMsg());

        Pattern billingPattern = Pattern.compile(billingRegex);
        Matcher billingMatcher = billingPattern.matcher(smsModel.getMsg());

        if (spendMatcher.matches()) {
            String amount = Formatter.nullToEmptyString(spendMatcher.group(2)) + Formatter.nullToEmptyString(spendMatcher.group(3)) + Formatter.nullToEmptyString(spendMatcher.group(4));
            float spentAmount = Float.parseFloat(amount.replaceAll(",", ""));
            String spendingCard = spendMatcher.group(5);
            String spentDate = spendMatcher.group(7);
            String spentAt = spendMatcher.group(6).trim();
            //float balanceAmount =  Float.parseFloat(Formatter.nullToEmptyString(spendMatcher.group(9)).replaceAll(",", ""));

            TranscationModel transcationModel = new TranscationModel();
            transcationModel.spendingCard = spendingCard;
            transcationModel.spentAt = spentAt;
            transcationModel.spentAmount = spentAmount;
            transcationModel.smsId = Integer.parseInt(smsModel.getId());
            transcationModel.bankName = Constants.BANKNAMEAMEX;
            transcationModel.spentDate = convertToDate(spentDate);
            return transcationModel;
        }
        else if (spendMatcher2.matches()) {
            String amount = Formatter.nullToEmptyString(spendMatcher2.group(2)) + Formatter.nullToEmptyString(spendMatcher2.group(3)) + Formatter.nullToEmptyString(spendMatcher2.group(4));
            float spentAmount = Float.parseFloat(amount.replaceAll(",", ""));
            String spendingCard = spendMatcher2.group(5);
            String spentDate = spendMatcher2.group(7);
            String spentAt = spendMatcher2.group(6).trim();
            //float balanceAmount =  Float.parseFloat(Formatter.nullToEmptyString(spendMatcher.group(9)).replaceAll(",", ""));

            TranscationModel transcationModel = new TranscationModel();
            transcationModel.spendingCard = spendingCard;
            transcationModel.spentAt = spentAt;
            transcationModel.spentAmount = spentAmount;
            transcationModel.smsId = Integer.parseInt(smsModel.getId());
            transcationModel.bankName = Constants.BANKNAMEAMEX;
            transcationModel.spentDate = convertToDate(spentDate);
            return transcationModel;
        }
        else if(reminderMatcher.matches())
        {
            String amount = Formatter.nullToEmptyString(reminderMatcher.group(4)) + Formatter.nullToEmptyString(reminderMatcher.group(5)) + Formatter.nullToEmptyString(reminderMatcher.group(6));
            float spentAmount = Float.parseFloat(amount.replaceAll(",", ""));
            String spendingCard = reminderMatcher.group(1);
            String spentDate = reminderMatcher.group(2);

            TranscationModel transcationModel = new TranscationModel();
            transcationModel.spendingCard = spendingCard;
            transcationModel.spentAmount = spentAmount;
            transcationModel.smsId = Integer.parseInt(smsModel.getId());
            transcationModel.bankName = Constants.BANKNAMEAMEX;
            transcationModel.spentDate = convertToDate(spentDate);
            return transcationModel;
        }
        else if(billingMatcher.matches())
        {
            String amount = Formatter.nullToEmptyString(billingMatcher.group(3)) + Formatter.nullToEmptyString(billingMatcher.group(4)) + Formatter.nullToEmptyString(billingMatcher.group(5));
            float spentAmount = Float.parseFloat(amount.replaceAll(",", ""));
            String spendingCard = billingMatcher.group(1);
            String spentDate = billingMatcher.group(6);

            TranscationModel transcationModel = new TranscationModel();
            transcationModel.spendingCard = spendingCard;
            transcationModel.spentAmount = spentAmount;
            transcationModel.smsId = Integer.parseInt(smsModel.getId());
            transcationModel.bankName = Constants.BANKNAMEAMEX;
            transcationModel.spentDate = convertToDate2(spentDate);
            return transcationModel;
        }

        return null;
    }


    private Date convertToDate(String spentDate) {
        DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = sourceFormat.parse(spentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private Date convertToDate2(String spentDate) {
        DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yy");
        Date date = null;
        try {
            date = sourceFormat.parse(spentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
