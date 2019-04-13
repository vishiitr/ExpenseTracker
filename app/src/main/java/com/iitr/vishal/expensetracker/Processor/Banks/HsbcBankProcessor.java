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
 * Created by Divya on 19-03-2018.
 */

public class HsbcBankProcessor implements BankProcessor.IBankProcessor {
    //private final String spendingRegex = ".*xxxx(\\d+).*INR\\s([\\d,.]+)\\son\\s([\\d\\/]+)\\sat\\s([a-zA-Z0-9\\s]*\\.?)\\..*";
    private static final String spendingRegex = ".*" + Constants.RegexConstants.Card + " is used for " + Constants.RegexConstants.Money + " on " + Constants.RegexConstants.DateWithNumber + " at " + Constants.RegexConstants.Merchant + ".*";
private static final String spendingRegex2 = ".*" + Constants.RegexConstants.Card + " has been used at" + Constants.RegexConstants.Merchant + " for " + Constants.RegexConstants.Money + " on " + Constants.RegexConstants.DateWithNumber + ".*";
    @Override
    public TranscationModel onSaveTranscation(SmsModel smsModel) {
        Pattern spendingPattern = Pattern.compile(spendingRegex);
        Pattern spendingPattern2 = Pattern.compile(spendingRegex2);
        Matcher spendingMatch = spendingPattern.matcher(smsModel.getMsg());
        Matcher spendingMatch2 = spendingPattern2.matcher(smsModel.getMsg());

        if (spendingMatch.matches()) {
            String amount = Formatter.nullToEmptyString(spendingMatch.group(3)) + Formatter.nullToEmptyString(spendingMatch.group(4)) + Formatter.nullToEmptyString(spendingMatch.group(5));
            float spentAmount = Float.parseFloat(amount.replaceAll(",", ""));
            String spendingCard = spendingMatch.group(1);
            String spentDate = spendingMatch.group(6);
            String spentAt = spendingMatch.group(7);

            TranscationModel transcationModel = new TranscationModel();
            transcationModel.spendingCard = spendingCard;
            transcationModel.spentAt = spentAt.trim();
            transcationModel.spentAmount = spentAmount;
            transcationModel.smsId = Integer.parseInt(smsModel.getId());
            transcationModel.bankName = Constants.BANKNAMEHSBC;
            transcationModel.spentDate = convertToDate(spentDate);
            return transcationModel;
        }
        if (spendingMatch2.matches()) {
            String amount = Formatter.nullToEmptyString(spendingMatch2.group(4)) + Formatter.nullToEmptyString(spendingMatch2.group(5)) + Formatter.nullToEmptyString(spendingMatch2.group(6));
            float spentAmount = Float.parseFloat(amount.replaceAll(",", ""));
            String spendingCard = spendingMatch2.group(1);
            String spentDate = spendingMatch2.group(7);
            String spentAt = spendingMatch2.group(2);

            TranscationModel transcationModel = new TranscationModel();
            transcationModel.spendingCard = spendingCard;
            transcationModel.spentAt = spentAt;
            transcationModel.spentAmount = spentAmount;
            transcationModel.smsId = Integer.parseInt(smsModel.getId());
            transcationModel.bankName = Constants.BANKNAMEHSBC;
            transcationModel.spentDate = convertToDate(spentDate);
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
}
