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
 * Created by Divya on 17-03-2018.
 */

public class IndusBankProcessor implements BankProcessor.IBankProcessor {
    //private final String spendingRegex = ".*XX(\\d{4}).*INR\\s([\\d,\\.]*) on ([0-9\\/]*).*at\\s([a-zA-Z0-9\\s]*\\.?)\\sis.*";
    private final static String spendingRegex = ".*" + "[xX]{2}(\\d{4})" + " for " + Constants.RegexConstants.Money +" on " + Constants.RegexConstants.DateWithNumber+".* at "+ "([a-zA-Z0-9 ]*)" + "is [Aa]pproved.*";

    public TranscationModel onSaveTranscation(SmsModel smsModel) {
        Pattern p = Pattern.compile(spendingRegex);
        Matcher m = p.matcher(smsModel.getMsg());

        if (m.matches()) {
            String amount = Formatter.nullToEmptyString(m.group(3)) + Formatter.nullToEmptyString(m.group(4)) + Formatter.nullToEmptyString(m.group(5));
            float spentAmount = Float.parseFloat(amount.replaceAll(",", ""));
            String spendingCard = m.group(1);
            String spentDate = m.group(6);
            String spentAt = m.group(7).trim();

            TranscationModel transcationModel = new TranscationModel();
            transcationModel.spendingCard = spendingCard;
            transcationModel.spentAt = spentAt;
            transcationModel.spentAmount = spentAmount;
            transcationModel.spendingCard = spentAt;
            transcationModel.smsId = Integer.parseInt(smsModel.getId());
            transcationModel.bankName = Constants.BANKNAMEINDUS;
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
