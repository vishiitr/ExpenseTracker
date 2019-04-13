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

public class IciciBankProcessor implements BankProcessor.IBankProcessor {
    //private final String spendingRegex = ".*INR\\s([\\d,\\.]+).*xxx(\\d+).*at\\s([a-zA-Z0-9\\s\\.]*)\\son\\s(.{9,11})\\..*";
    private static final String spendingRegex = ".*" + Constants.RegexConstants.Money + " using .*" + Constants.RegexConstants.Card + ".* at " + Constants.RegexConstants.Merchant + " on " + Constants.RegexConstants.DateWithName + ".*Avbl.*" +Constants.RegexConstants.Money + ".*";
    private static final String spendingRegex2 = Constants.RegexConstants.Money + " debited .*" + Constants.RegexConstants.Card + " on " + Constants.RegexConstants.DateWithName + ".Info:" + Constants.RegexConstants.Merchant + ".*Avbl.*" +Constants.RegexConstants.Money + ".*";
    private final static String billingRegex = ".*stmt for.*" + "[xX]{2}(\\d{4})" + ".* of " + Constants.RegexConstants.Money +" or.*" + ".*by " + Constants.RegexConstants.DateWithName + "\\.";

    @Override
    public TranscationModel onSaveTranscation(SmsModel smsModel) {
        Pattern p = Pattern.compile(spendingRegex);
        Matcher m = p.matcher(smsModel.getMsg());

        Pattern p1 = Pattern.compile(spendingRegex2);
        Matcher m1 = p1.matcher(smsModel.getMsg());

        Pattern billingPattern = Pattern.compile(billingRegex);
        Matcher billingMatcher = billingPattern.matcher(smsModel.getMsg());

        if (m.matches()) {
            String amount = Formatter.nullToEmptyString(m.group(2)) + Formatter.nullToEmptyString(m.group(3)) + Formatter.nullToEmptyString(m.group(4));
            float spentAmount = Float.parseFloat(amount.replaceAll(",", ""));
            String spendingCard = m.group(5);
            String spentDate = m.group(7);
            String spentAt = m.group(6);
            String availableBalanace = Formatter.nullToEmptyString(m.group(11));

            TranscationModel transcationModel = new TranscationModel();
            transcationModel.spendingCard = spendingCard;
            transcationModel.spentAt = spentAt.trim();
            transcationModel.spentAmount = spentAmount;
            transcationModel.smsId = Integer.parseInt(smsModel.getId());
            transcationModel.bankName = Constants.BANKNAMEICICI;
            transcationModel.spentDate = convertToDate(spentDate);
            transcationModel.availableBalance = Float.parseFloat(availableBalanace.replaceAll(",", ""));
            return transcationModel;
        }else if (m1.matches()) {
            String amount = Formatter.nullToEmptyString(m1.group(2)) + Formatter.nullToEmptyString(m1.group(3)) + Formatter.nullToEmptyString(m1.group(4));
            float spentAmount = Float.parseFloat(amount.replaceAll(",", ""));
            String spendingCard = m1.group(5);
            String spentDate = m1.group(6);
            String spentAt = m1.group(7);
            String availableBalanace = Formatter.nullToEmptyString(m1.group(11));

            TranscationModel transcationModel = new TranscationModel();
            transcationModel.spendingCard = spendingCard;
            transcationModel.spentAt = spentAt.trim();
            transcationModel.spentAmount = spentAmount;
            transcationModel.smsId = Integer.parseInt(smsModel.getId());
            transcationModel.bankName = Constants.BANKNAMEICICI;
            transcationModel.spentDate = convertToDate(spentDate);
            transcationModel.availableBalance = Float.parseFloat(availableBalanace.replaceAll(",", ""));
            return transcationModel;
        }
        else if (billingMatcher.matches()) {
            String amount = Formatter.nullToEmptyString(billingMatcher.group(3)) + Formatter.nullToEmptyString(billingMatcher.group(4)) + Formatter.nullToEmptyString(billingMatcher.group(5));
            float spentAmount = Float.parseFloat(amount.replaceAll(",", ""));
            String spendingCard = billingMatcher.group(1);
            String spentDate = billingMatcher.group(6);

            TranscationModel transcationModel = new TranscationModel();
            transcationModel.spendingCard = spendingCard;
            transcationModel.spentAmount = spentAmount;
            transcationModel.smsId = Integer.parseInt(smsModel.getId());
            transcationModel.bankName = Constants.BANKNAMEICICI;
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
