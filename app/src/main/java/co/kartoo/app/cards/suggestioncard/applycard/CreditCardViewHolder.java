package co.kartoo.app.cards.suggestioncard.applycard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import co.kartoo.app.R;
import co.kartoo.app.cards.suggestioncard.applycard.ApplyCardInterface;
import co.kartoo.app.rest.model.newest.CreditCard;
import de.greenrobot.event.EventBus;

public class CreditCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView mTVcardName, mTVcardValid, mTVcardNumber;
    public ImageView mIVdelete;
    Context mContext;
    CreditCard creditCard;

    public CreditCardViewHolder(Context mContext, View itemView) {
        super(itemView);
        this.mContext = mContext;
        mIVdelete = (ImageView) itemView.findViewById(R.id.mIVdelete);
        mTVcardName = (TextView) itemView.findViewById(R.id.mTVcardName);
        mTVcardValid = (TextView) itemView.findViewById(R.id.mTVcardValid);
        mTVcardNumber = (TextView) itemView.findViewById(R.id.mTVcardNumber);

        mIVdelete.setOnClickListener(this);
    }

    public void bind(CreditCard creditCard) {
        mTVcardName.setText(creditCard.getName());
        mTVcardValid.setText(creditCard.getValid());
        mTVcardNumber.setText(maskString(creditCard.getCvv(),0,12,'*'));
    }


    private static String maskString(String strText, int start, int end, char maskChar){
        if(strText == null || strText.equals(""))
            return "";
        if(start < 0)
            start = 0;
        if( end > strText.length() )
            end = strText.length();
        int maskLength = end - start;
        if(maskLength == 0)
            return strText;
        StringBuilder sbMaskString = new StringBuilder(maskLength);
        for(int i = 0; i < maskLength; i++){
            sbMaskString.append(maskChar);
        }
        return strText.substring(0, start)
                + sbMaskString.toString()
                + strText.substring(start + maskLength);
    }

    @Override
    public void onClick(View v) {
        if(v == mIVdelete){
            EventBus.getDefault().postSticky(new DeleteCreditCardEvent(creditCard));

            ((ApplyCardInterface) v.getContext()).userItemClick(getAdapterPosition());
        }
    }
}
