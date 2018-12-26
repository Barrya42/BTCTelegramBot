package root.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "data",
        "timestamp"
})
public class RateResponseEntity
{

    @JsonProperty("data")
    private Data data;
    @JsonProperty("timestamp")
    private Long timestamp;

    @JsonProperty("data")
    public Data getData()
    {
        return data;
    }

    @JsonProperty("data")
    public void setData(Data data)
    {
        this.data = data;
    }

    @JsonProperty("timestamp")
    public Long getTimestamp()
    {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(Long timestamp)
    {
        this.timestamp = timestamp;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "id",
            "symbol",
            "currencySymbol",
            "type",
            "rateUsd"
    })
    public class Data
    {

        @JsonProperty("id")
        private String id;
        @JsonProperty("symbol")
        private String symbol;
        @JsonProperty("currencySymbol")
        private String currencySymbol;
        @JsonProperty("type")
        private String type;
        @JsonProperty("rateUsd")
        private String rateUsd;

        @JsonProperty("id")
        public String getId()
        {
            return id;
        }

        @JsonProperty("id")
        public void setId(String id)
        {
            this.id = id;
        }

        @JsonProperty("symbol")
        public String getSymbol()
        {
            return symbol;
        }

        @JsonProperty("symbol")
        public void setSymbol(String symbol)
        {
            this.symbol = symbol;
        }

        @JsonProperty("currencySymbol")
        public String getCurrencySymbol()
        {
            return currencySymbol;
        }

        @JsonProperty("currencySymbol")
        public void setCurrencySymbol(String currencySymbol)
        {
            this.currencySymbol = currencySymbol;
        }

        @JsonProperty("type")
        public String getType()
        {
            return type;
        }

        @JsonProperty("type")
        public void setType(String type)
        {
            this.type = type;
        }

        @JsonProperty("rateUsd")
        public String getRateUsd()
        {
            return rateUsd;
        }

        @JsonProperty("rateUsd")
        public void setRateUsd(String rateUsd)
        {
            this.rateUsd = rateUsd;
        }

    }
}