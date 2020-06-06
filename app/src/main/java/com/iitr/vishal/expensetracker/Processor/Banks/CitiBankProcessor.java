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

public class CitiBankProcessor implements BankProcessor.IBankProcessor {
    //private final String spendingRegex = "(Rs\\.|Rs\\s|INR\\s)?([\\d,\\.]*).*spent.*XXX(\\d+)\\son\\s(.{9,11})\\sat\\s([a-zA-Z0-9\\s]+\\.?).*";
    private final static String spendingRegex = Constants.RegexConstants.Money + ".* spent .*" + Constants.RegexConstants.Card + " on " + Constants.RegexConstants.DateWithName + " at " + Constants.RegexConstants.Merchant + ".*available.*" + Constants.RegexConstants.Money +"\\..*";
    private final static String spendingRegexMarc2019 = Constants.RegexConstants.Money + ".* spent .*" + "(\\d{4})" + " on " + Constants.RegexConstants.DateWithName + " at " + Constants.RegexConstants.Merchant + ".*available.*" + Constants.RegexConstants.Money +"\\..*";
    private final static String reminderRegex = "Reminder: .*\\*\\*\\*(\\d{4}).*" + Constants.RegexConstants.DateWithName + ".*=" + Constants.RegexConstants.Money + ",Minimum.*";
    private final static String billingRegex = "Mini Statement .*\\*\\*\\*(\\d{4}).*" + Constants.RegexConstants.Money +".*Minimum.*" + Constants.RegexConstants.DateWithName +".*details.*";
    @Override
    public TranscationModel onSaveTranscation(SmsModel smsModel) {
        Pattern spendPattern = Pattern.compile(spendingRegex);
        Matcher spendMatcher = spendPattern.matcher(smsModel.getMsg());

        Pattern spendPatternMar2019 = Pattern.compile(spendingRegexMarc2019);
        Matcher spendMatcherMar2019 = spendPatternMar2019.matcher(smsModel.getMsg());

        Pattern reminderPattern = Pattern.compile(reminderRegex);
        Matcher reminderMatcher = reminderPattern.matcher(smsModel.getMsg());

        Pattern billingPattern = Pattern.compile(billingRegex);
        Matcher billingMatcher = billingPattern.matcher(smsModel.getMsg());

        if (spendMatcherMar2019.matches()) {
            String amount = Formatter.nullToEmptyString(spendMatcherMar2019.group(2)) + Formatter.nullToEmptyString(spendMatcherMar2019.group(3)) + Formatter.nullToEmptyString(spendMatcherMar2019.group(4));
            float spentAmount = Float.parseFloat(amount.replaceAll(",", ""));
            String spendingCard = spendMatcherMar2019.group(5);
            String spentDate = spendMatcherMar2019.group(6);
            String spentAt = spendMatcherMar2019.group(7).trim();
            float balanceAmount =  Float.parseFloat(Formatter.nullToEmptyString(spendMatcherMar2019.group(9)).replaceAll(",", ""));

            TranscationModel transcationModel = new TranscationModel();
            transcationModel.spendingCard = spendingCard;
            transcationModel.spentAt = spentAt;
            transcationModel.spentAmount = spentAmount;
            transcationModel.smsId = Integer.parseInt(smsModel.getId());
            transcationModel.bankName = Constants.BANKNAMECITI;
            transcationModel.spentDate = convertToDate(spentDate);
            transcationModel.availableBalance = balanceAmount;
            return transcationModel;
        }
        if (spendMatcher.matches()) {
            String amount = Formatter.nullToEmptyString(spendMatcher.group(2)) + Formatter.nullToEmptyString(spendMatcher.group(3)) + Formatter.nullToEmptyString(spendMatcher.group(4));
            float spentAmount = Float.parseFloat(amount.replaceAll(",", ""));
            String spendingCard = spendMatcher.group(5);
            String spentDate = spendMatcher.group(6);
            String spentAt = spendMatcher.group(7).trim();
            float balanceAmount =  Float.parseFloat(Formatter.nullToEmptyString(spendMatcher.group(9)).replaceAll(",", ""));

            TranscationModel transcationModel = new TranscationModel();
            transcationModel.spendingCard = spendingCard;
            transcationModel.spentAt = spentAt;
            transcationModel.spentAmount = spentAmount;
            transcationModel.smsId = Integer.parseInt(smsModel.getId());
            transcationModel.bankName = Constants.BANKNAMECITI;
            transcationModel.spentDate = convertToDate(spentDate);
            transcationModel.availableBalance = balanceAmount;
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
            transcationModel.bankName = Constants.BANKNAMECITI;
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
            transcationModel.bankName = Constants.BANKNAMECITI;
            transcationModel.spentDate = convertToDate(spentDate);
            return transcationModel;
        }

        return null;
    }


    private Date convertToDate(String spentDate) {
        DateFormat sourceFormat = new SimpleDateFormat("dd-MMM-yy");
        Date date = null;
        try {
            date = sourceFormat.parse(spentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
