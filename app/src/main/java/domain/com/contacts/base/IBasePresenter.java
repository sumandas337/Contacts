package domain.com.contacts.base;

/**
 * Created by sumandas on 29/01/2017.
 */

public interface IBasePresenter {

    void setMvpView(IBaseView baseView);

    void create();

    void destroy();

    IBaseView getView();
}
