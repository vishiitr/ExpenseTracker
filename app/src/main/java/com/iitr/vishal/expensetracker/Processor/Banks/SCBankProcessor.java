package com.iitr.vishal.expensetracker.Processor.Banks;

import com.iitr.vishal.expensetracker.Common.Constants;
import com.iitr.vishal.expensetracker.Common.Formatter;
import com.iitr.vishal.expensetracker.Model.SmsModel;
import com.iitr.vishal.expensetracker.Model.TranscationModel;
import com.iitr.vishal.expensetracker.Processor.BankProcessor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Divya on 07-10-2018.
 */

public class SCBankProcessor implements BankProcessor.IBankProcessor {
    private final static String spendingRegex = ".*Card No " + Constants.RegexConstants.Card + " on " + Constants.RegexConstants.DateWithNumber + " for " + Constants.RegexConstants.Money + ".*";
    private final static String reminderRegex = "Dear .*" + Constants.RegexConstants.Money + ".*" + "XXX(\\d{4}).*" + Constants.RegexConstants.DateWithName + ".*";
    private final static String billingRegex = "Mini Statement .*\\*\\*\\*(\\d{4}).*" + Constants.RegexConstants.Money + ".*Minimum.*" + Constants.RegexConstants.DateWithName + ".*Refer.*";

    @Override
    public TranscationModel onSaveTranscation(SmsModel smsModel) {
        Pattern spendPattern = Pattern.compile(spendingRegex);
        Matcher spendMatcher = spendPattern.matcher(smsModel.getMsg());

        Pattern reminderPattern = Pattern.compile(reminderRegex);
        Matcher reminderMatcher = reminderPattern.matcher(smsModel.getMsg());

        Pattern billingPattern = Pattern.compile(billingRegex);
        Matcher billingMatcher = billingPattern.matcher(smsModel.getMsg());

        if (spendMatcher.matches()) {
            String amount = Formatter.nullToEmptyString(spendMatcher.group(4)) + Formatter.nullToEmptyString(spendMatcher.group(5)) + Formatter.nullToEmptyString(spendMatcher.group(6));
            float spentAmount = Float.parseFloat(amount.replaceAll(",", ""));
            String spendingCard = spendMatcher.group(1);
            String spentDate = spendMatcher.group(2);
            String spentAt = "Default-SC";//spendMatcher.group(6);

            TranscationModel transcationModel = new TranscationModel();
            transcationModel.spendingCard = spendingCard;
            transcationModel.spentAt = spentAt;
            transcationModel.spentAmount = spentAmount;
            transcationModel.smsId = Integer.parseInt(smsModel.getId());
            transcationModel.bankName = Constants.BANKNAMESBI;
            transcationModel.spentDate = convertToDate(spentDate);
            return transcationModel;
        } else if (reminderMatcher.matches()) {
            String amount = Formatter.nullToEmptyString(reminderMatcher.group(2)) + Formatter.nullToEmptyString(reminderMatcher.group(3)) + Formatter.nullToEmptyString(reminderMatcher.group(4));
            float spentAmount = Float.parseFloat(amount.replaceAll(",", ""));
            String spendingCard = reminderMatcher.group(5);
            String spentDate = reminderMatcher.group(6);

            TranscationModel transcationModel = new TranscationModel();
            transcationModel.spendingCard = spendingCard;
            transcationModel.spentAmount = spentAmount;
            transcationModel.smsId = Integer.parseInt(smsModel.getId());
            transcationModel.bankName = Constants.BANKNAMESBI;
            transcationModel.spentDate = convertToDate2(spentDate);
            return transcationModel;
        } else if (billingMatcher.matches()) {
            String amount = Formatter.nullToEmptyString(billingMatcher.group(3)) + Formatter.nullToEmptyString(billingMatcher.group(4)) + Formatter.nullToEmptyString(billingMatcher.group(5));
            float spentAmount = Float.parseFloat(amount.replaceAll(",", ""));
            String spendingCard = billingMatcher.group(1);
            String spentDate = billingMatcher.group(6);

            TranscationModel transcationModel = new TranscationModel();
            transcationModel.spendingCard = spendingCard;
            transcationModel.spentAmount = spentAmount;
            transcationModel.smsId = Integer.parseInt(smsModel.getId());
            transcationModel.bankName = Constants.BANKNAMESBI;
            transcationModel.spentDate = convertToDate(spentDate);
            return transcationModel;
        }

        return null;
    }


    private Date convertToDate(String spentDate) {
        DateFormat sourceFormat = new SimpleDateFormat("dd/mm/yy");
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
}
