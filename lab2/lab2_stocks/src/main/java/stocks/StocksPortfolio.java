package stocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StocksPortfolio {
    IStockmarketService stockmarket;
    List<Stock> stocks;

    public StocksPortfolio(IStockmarketService stockmarket) {
        this.stockmarket = stockmarket;
        stocks = new ArrayList<>();
    }

    public void addStock(Stock stock) throws IllegalArgumentException {
        if (stock == null) {
            throw new IllegalArgumentException();
        }

        stocks.add(stock);
    }

    public double totalValue() {
        Double total = 0d;
        for (Stock stock : stocks) {
            total += stockmarket.lookUpPrice(stock.label) * stock.quantity;
        }
        return total;
    }

    /**
     * @param topN the number of most valuable stocks to return
     * @return a list with the topN most valuable stocks in the portfolio
     */
    public List<Stock> mostValuableStocks(int topN) {
        topN = Math.min(stocks.size(), topN);

        ArrayList<Stock> topNStocks = new ArrayList<>(topN);
        var x = stocks.stream().sorted().iterator();

        for (int i = 0; i < topN; i++) {
            topNStocks.add(x.next());
        }

        return topNStocks;
    }
}
