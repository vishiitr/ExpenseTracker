package com.iitr.vishal.expensetracker.Processor.Banks;

import com.iitr.vishal.expensetracker.Common.Constants;
import com.iitr.vishal.expensetracker.Common.Formatter;
import com.iitr.vishal.expensetracker.Model.SmsModel;
import com.iitr.vishal.expensetracker.Model.TranscationModel;
import com.iitr.vishal.expensetracker.Processor.BankProcessor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Divya on 17-03-2018.
 */

public class IndusBankProcessor implements BankProcessor.IBankProcessor {
    //private final String spendingRegex = ".*XX(\\d{4}).*INR\\s([\\d,\\.]*) on ([0-9\\/]*).*at\\s([a-zA-Z0-9\\s]*\\.?)\\sis.*";
    private final static String spendingRegex = ".*" + "[xX]{2}(\\d{4})" + " for " + Constants.RegexConstants.Money + " on " + Constants.RegexConstants.DateWithNumber + ".* at " + Constants.RegexConstants.Merchant + ".*is [Aa]pproved.*";
    private final static String reminderRegex = ".*Stmt Alert.*" + Constants.RegexConstants.Card + " is.*" + Constants.RegexConstants.Money + " and.*" + Constants.RegexConstants.DateWithName;
    private final static String billingRegex = ".*e-Stmt.*" + Constants.RegexConstants.Card + ".* due are " + "Constants.RegexConstants.Money.*" + "is " + Constants.RegexConstants.Money + ".*and.*"+ Constants.RegexConstants.Money + ".*";

    public TranscationModel onSaveTranscation(SmsModel smsModel) {
        Pattern spendPattern = Pattern.compile(spendingRegex);
        Matcher spendMatcher = spendPattern.matcher(smsModel.getMsg());

        Pattern reminderPattern = Pattern.compile(reminderRegex);
        Matcher reminderMatcher = reminderPattern.matcher(smsModel.getMsg());

        Pattern billingPattern = Pattern.compile(billingRegex);
        Matcher billingMatcher = billingPattern.matcher(smsModel.getMsg());

        if (spendMatcher.matches()) {
            String amount = Formatter.nullToEmptyString(spendMatcher.group(3)) + Formatter.nullToEmptyString(spendMatcher.group(4)) + Formatter.nullToEmptyString(spendMatcher.group(5));
            float spentAmount = Float.parseFloat(amount.replaceAll(",", ""));
            String spendingCard = spendMatcher.group(1);
            String spentDate = spendMatcher.group(6);
            String spentAt = spendMatcher.group(7).trim();

            TranscationModel transcationModel = new TranscationModel();
            transcationModel.spendingCard = spendingCard;
            transcationModel.spentAt = spentAt.trim();
            transcationModel.spentAmount = spentAmount;
            transcationModel.smsId = Integer.parseInt(smsModel.getId());
            transcationModel.bankName = Constants.BANKNAMEINDUS;
            transcationModel.spentDate = convertToDate(spentDate);
            return transcationModel;
        }
        else if (reminderMatcher.matches()) {
            String amount = Formatter.nullToEmptyString(reminderMatcher.group(3)) + Formatter.nullToEmptyString(reminderMatcher.group(4)) + Formatter.nullToEmptyString(reminderMatcher.group(5));
            float spentAmount = Float.parseFloat(amount.replaceAll(",", ""));
            String spendingCard = reminderMatcher.group(1);
            String spentDate = reminderMatcher.group(6);

            TranscationModel transcationModel = new TranscationModel();
            transcationModel.spendingCard = spendingCard;
            transcationModel.spentAmount = spentAmount;
            transcationModel.smsId = Integer.parseInt(smsModel.getId());
            transcationModel.bankName = Constants.BANKNAMEINDUS;
            transcationModel.spentDate = convertToDate2(spentDate);
            return transcationModel;
        }
        else if (billingMatcher.matches()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(smsModel.getSmsDate());

            String amount = Formatter.nullToEmptyString(billingMatcher.group(4)) + Formatter.nullToEmptyString(billingMatcher.group(5)) + Formatter.nullToEmptyString(billingMatcher.group(6));
            float spentAmount = Float.parseFloat(amount.replaceAll(",", ""));
            String spendingCard = billingMatcher.group(1);
            String spentDate = billingMatcher.group(2) + "-" + calendar.get(Calendar.YEAR);

            TranscationModel transcationModel = new TranscationModel();
            transcationModel.spendingCard = spendingCard;
            transcationModel.spentAmount = spentAmount;
            transcationModel.smsId = Integer.parseInt(smsModel.getId());
            transcationModel.bankName = Constants.BANKNAMEINDUS;
            transcationModel.spentDate = convertToDate2(spentDate);
            return transcationModel;
        }
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
        DateFormat sourceFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = null;
        try {
            date = sourceFormat.parse(spentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
