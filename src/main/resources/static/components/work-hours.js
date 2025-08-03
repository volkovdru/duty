// Компонент для расчета рабочих часов
function WorkHoursTable({ users, workHours, onSaveWorkHours, onDeleteWorkHours }) {
    const [selectedDate, setSelectedDate] = React.useState(new Date().toISOString().split('T')[0]);
    const [workHoursData, setWorkHoursData] = React.useState([]);
    const [loading, setLoading] = React.useState(false);

    // Загрузка данных рабочих часов
    React.useEffect(() => {
        if (selectedDate) {
            loadWorkHoursData();
        }
    }, [selectedDate]);

    const loadWorkHoursData = async () => {
        setLoading(true);
        try {
            const response = await fetch(`/api/work-hours/date/${selectedDate}`);
            if (response.ok) {
                const data = await response.json();
                setWorkHoursData(data);
            }
        } catch (error) {
            console.error('Ошибка загрузки данных рабочих часов:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleDateChange = (e) => {
        setSelectedDate(e.target.value);
    };

    const handleWorkHoursChange = (userId, field, value) => {
        setWorkHoursData(prev => 
            prev.map(item => 
                item.userId === userId 
                    ? { ...item, [field]: parseFloat(value) || 0 }
                    : item
            )
        );
    };

    const calculateTotalHours = (dayHours, nightHours) => {
        return (parseFloat(dayHours) || 0) + (parseFloat(nightHours) || 0);
    };

    const handleSave = async () => {
        try {
            for (const item of workHoursData) {
                const totalHours = calculateTotalHours(item.dayHours, item.nightHours);
                const updatedItem = { ...item, totalHours };

                if (item.id) {
                    await fetch(`/api/work-hours/${item.id}`, {
                        method: 'PUT',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(updatedItem)
                    });
                } else {
                    await fetch('/api/work-hours', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(updatedItem)
                    });
                }
            }
            loadWorkHoursData();
        } catch (error) {
            console.error('Ошибка сохранения:', error);
        }
    };

    const calculateOvertime = (totalHours) => {
        const standardHours = 8; // Стандартный рабочий день
        return Math.max(0, totalHours - standardHours);
    };

    return (
        <div className="work-hours-container">
            <div className="work-hours-header">
                <h2>Расчет рабочих часов</h2>
                <div className="date-selector">
                    <label>Дата: </label>
                    <input 
                        type="date" 
                        value={selectedDate} 
                        onChange={handleDateChange}
                        className="form-control"
                    />
                </div>
                <button 
                    className="btn btn-primary" 
                    onClick={handleSave}
                    disabled={loading}
                >
                    {loading ? 'Сохранение...' : 'Сохранить'}
                </button>
            </div>

            <div className="work-hours-table">
                <table className="table table-striped">
                    <thead>
                        <tr>
                            <th>Сотрудник</th>
                            <th>Дневные часы</th>
                            <th>Ночные часы</th>
                            <th>Всего часов</th>
                            <th>Сверхурочные</th>
                            <th>Примечания</th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.map(user => {
                            const userWorkHours = workHoursData.find(w => w.userId === user.id) || {
                                userId: user.id,
                                userName: user.name,
                                dayHours: 0,
                                nightHours: 0,
                                totalHours: 0,
                                overtimeHours: 0,
                                notes: ''
                            };

                            const totalHours = calculateTotalHours(userWorkHours.dayHours, userWorkHours.nightHours);
                            const overtimeHours = calculateOvertime(totalHours);

                            return (
                                <tr key={user.id}>
                                    <td>{user.name}</td>
                                    <td>
                                        <input 
                                            type="number" 
                                            className="form-control"
                                            value={userWorkHours.dayHours || 0}
                                            onChange={(e) => handleWorkHoursChange(user.id, 'dayHours', e.target.value)}
                                            min="0"
                                            step="0.5"
                                        />
                                    </td>
                                    <td>
                                        <input 
                                            type="number" 
                                            className="form-control"
                                            value={userWorkHours.nightHours || 0}
                                            onChange={(e) => handleWorkHoursChange(user.id, 'nightHours', e.target.value)}
                                            min="0"
                                            step="0.5"
                                        />
                                    </td>
                                    <td>
                                        <span className="total-hours">{totalHours}</span>
                                    </td>
                                    <td>
                                        <span className="overtime-hours">{overtimeHours}</span>
                                    </td>
                                    <td>
                                        <input 
                                            type="text" 
                                            className="form-control"
                                            value={userWorkHours.notes || ''}
                                            onChange={(e) => handleWorkHoursChange(user.id, 'notes', e.target.value)}
                                            placeholder="Примечания"
                                        />
                                    </td>
                                </tr>
                            );
                        })}
                    </tbody>
                </table>
            </div>

            <div className="work-hours-summary">
                <h3>Сводка за день</h3>
                <div className="summary-stats">
                    <div className="stat-item">
                        <strong>Всего дневных часов:</strong> 
                        {workHoursData.reduce((sum, item) => sum + (parseFloat(item.dayHours) || 0), 0)}
                    </div>
                    <div className="stat-item">
                        <strong>Всего ночных часов:</strong> 
                        {workHoursData.reduce((sum, item) => sum + (parseFloat(item.nightHours) || 0), 0)}
                    </div>
                    <div className="stat-item">
                        <strong>Общее количество часов:</strong> 
                        {workHoursData.reduce((sum, item) => sum + calculateTotalHours(item.dayHours, item.nightHours), 0)}
                    </div>
                    <div className="stat-item">
                        <strong>Общие сверхурочные:</strong> 
                        {workHoursData.reduce((sum, item) => sum + calculateOvertime(calculateTotalHours(item.dayHours, item.nightHours)), 0)}
                    </div>
                </div>
            </div>
        </div>
    );
}

// Экспорт для использования в основном файле
if (typeof module !== 'undefined' && module.exports) {
    module.exports = WorkHoursTable;
} 