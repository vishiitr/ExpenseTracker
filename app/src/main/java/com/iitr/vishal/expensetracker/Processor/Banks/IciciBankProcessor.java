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

public class IciciBankProcessor implements BankProcessor.IBankProcessor {
    //private final String spendingRegex = ".*INR\\s([\\d,\\.]+).*xxx(\\d+).*at\\s([a-zA-Z0-9\\s\\.]*)\\son\\s(.{9,11})\\..*";
    private static final String spendingRegex = ".*" + Constants.RegexConstants.Money + " using .*" + Constants.RegexConstants.Card + ".* at " + Constants.RegexConstants.Merchant + " on " + Constants.RegexConstants.DateWithName + ".*";

    @Override
    public TranscationModel onSaveTranscation(SmsModel smsModel) {
        Pattern p = Pattern.compile(spendingRegex);
        Matcher m = p.matcher(smsModel.getMsg());

        if (m.matches()) {
            String amount = Formatter.nullToEmptyString(m.group(2)) + Formatter.nullToEmptyString(m.group(3)) + Formatter.nullToEmptyString(m.group(4));
            float spentAmount = Float.parseFloat(amount.replaceAll(",", ""));
            String spendingCard = m.group(5);
            String spentDate = m.group(7);
            String spentAt = m.group(6);

            TranscationModel transcationModel = new TranscationModel();
            transcationModel.spendingCard = spendingCard;
            transcationModel.spentAt = spentAt;
            transcationModel.spentAmount = spentAmount;
            transcationModel.spendingCard = spentAt;
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
