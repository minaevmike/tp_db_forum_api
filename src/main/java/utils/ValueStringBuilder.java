package utils;

/**
 * Created by Andrey
 * 22.04.14.
 */
public class ValueStringBuilder {

    StringBuilder sb;
    int counter;

    public ValueStringBuilder() {
        sb = new StringBuilder();
        counter = 0;
    }

    public ValueStringBuilder(String str) {
        sb = new StringBuilder(str);
        counter = 0;
    }

    public ValueStringBuilder append(String str)
    {
        count();
        if(str == null) {
            sb.append("NULL");
        } else {
            sb.append("'").append(str).append("'");
        }
        return this;
    }

    public ValueStringBuilder append(boolean bool)
    {
        count();
        sb.append(bool);
        return this;
    }

    public ValueStringBuilder append(int i)
    {
        count();
        sb.append(i);
        return this;
    }

    public void close()
    {
        sb.append(")");
    }


    private void count()
    {
        if(counter++ > 0)
            sb.append(",");
    }

    @Override
    public String toString()
    {
        return sb.toString();
    }
}
