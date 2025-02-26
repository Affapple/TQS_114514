package stocks;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(MockitoExtension.class)
public class StocksPortfolioTest {

    @Mock
    IStockmarketService market;

    @InjectMocks
    StocksPortfolio portfolio;

    @Test
    public void whenNoStocks_thenValueIsZero() {
        assertThat(portfolio.totalValue(), equalTo(0d));
        Mockito.verify(market, Mockito.times(0)).lookUpPrice(Mockito.anyString());
    }

    @Test
    public void whenAddingNullStock_thenIllegalArgumentException() {
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> portfolio.addStock(null),
                "No exception was thrown when adding a null stock");

    }

    @Test
    public void whenStocks_thenValueIsExact() {
        Mockito.when(market.lookUpPrice("EBAY")).thenReturn(50d);
        Mockito.when(market.lookUpPrice("TSLA")).thenReturn(100d);
        Mockito.when(market.lookUpPrice("MSFT")).thenReturn(150d);

        portfolio.addStock(new Stock("EBAY", 1));
        portfolio.addStock(new Stock("TSLA", 3));
        portfolio.addStock(new Stock("MSFT", 5));

        assertThat(portfolio.totalValue(), equalTo((1 * 50d) + (3 * 100) + (5 * 150)));

        Mockito.verify(market, Mockito.times(3)).lookUpPrice(Mockito.anyString());
    }

    @Test
    public void whenAddingOwnedStock_thenValueIsCorrect() {
        double TSLA = 100d, EBAY = 50d;

        Mockito.when(market.lookUpPrice("EBAY")).thenReturn(EBAY);
        Mockito.when(market.lookUpPrice("TSLA")).thenReturn(TSLA);

        portfolio.addStock(new Stock("EBAY", 1));
        portfolio.addStock(new Stock("TSLA", 3));

        assertThat(
                portfolio.totalValue(),
                equalTo((1 * EBAY) + (3 * TSLA)));
        Mockito.verify(market, Mockito.times(2)).lookUpPrice(Mockito.anyString());

        portfolio.addStock(new Stock("TSLA", 5));
        assertThat(
                portfolio.totalValue(),
                equalTo((1 * EBAY) + (3 * TSLA) + (5 * TSLA)));

        Mockito.verify(market, Mockito.times(5)).lookUpPrice(Mockito.anyString());
    }
}
