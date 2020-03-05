package atdd.path.application;

import atdd.path.SoftAssertionTest;
import atdd.path.application.dto.favorite.FavoriteCreateResponseView;
import atdd.path.application.dto.favorite.FavoriteListResponseView;
import atdd.path.dao.FavoriteDao;
import atdd.path.domain.Edge;
import atdd.path.domain.Favorite;
import atdd.path.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static atdd.path.TestConstant.STATION_NAME;
import static atdd.path.dao.FavoriteDao.EDGE_TYPE;
import static atdd.path.dao.FavoriteDao.STATION_TYPE;
import static atdd.path.fixture.FavoriteFixture.*;
import static atdd.path.fixture.UserFixture.KIM_NAME;
import static atdd.path.fixture.UserFixture.NEW_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FavoriteServiceTest extends SoftAssertionTest {
    private FavoriteService favoriteService;
    @MockBean
    private FavoriteDao favoriteDao;

    @BeforeEach
    void setUp() {
        this.favoriteService = new FavoriteService(favoriteDao);
    }

    @DisplayName("사용자가 지하철역 즐겨찾기를 등록 가능한지")
    @Test
    public void save() {
        //given
        when(favoriteDao.save(any(), eq(STATION_TYPE))).thenReturn(NEW_STATION_FAVORITE);

        //when
        FavoriteCreateResponseView favorite = favoriteService.save(NEW_USER, STATION_FAVORITE_CREATE_REQUEST_VIEW);
        Station station = (Station) favorite.getItem();
        //then
        assertThat(station.getName()).isEqualTo(STATION_NAME);
    }

    @DisplayName("사용자가 등록되어 있는 지하철역 즐겨찾기 목록을 조회가능한지")
    @Test
    public void findStationByUser() {
        //given
        when(favoriteDao.findStationByUser(any())).thenReturn(NEW_STATION_FAVORITES);

        //when
        FavoriteListResponseView favorites = favoriteService.findByUser(NEW_USER, STATION_TYPE);

        Favorite firstFavorite = favorites.getFirstIndex();
        Station station = (Station) firstFavorite.getItem();
        //then
        assertThat(favorites.getSize()).isGreaterThan(1);
        assertThat(station.getName()).isEqualTo(STATION_NAME);
        assertThat(firstFavorite.getUser().getName()).isEqualTo(KIM_NAME);
    }

    @DisplayName("사용자가 등록되어 있는 지하철 경로 즐겨찾기 목록을 조회가능한지")
    @Test
    public void findEdgeByUser() {
        //given
        when(favoriteDao.findEdgeByUser(any())).thenReturn(NEW_EDGE_FAVORITES);

        //when
        FavoriteListResponseView favorites = favoriteService.findByUser(NEW_USER, EDGE_TYPE);

        Favorite firstFavorite = favorites.getFirstIndex();
        Edge edge = (Edge) firstFavorite.getItem();
        //then
        assertThat(favorites.getSize()).isGreaterThan(1);
        assertThat(edge.getSourceStation().getName()).isEqualTo(STATION_NAME);
        assertThat(firstFavorite.getUser().getName()).isEqualTo(KIM_NAME);
    }

}